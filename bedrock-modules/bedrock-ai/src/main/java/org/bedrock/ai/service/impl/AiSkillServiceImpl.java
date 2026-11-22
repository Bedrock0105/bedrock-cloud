package org.bedrock.ai.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bedrock.ai.constant.AiConstant;
import org.bedrock.ai.entity.AiSkill;
import org.bedrock.ai.entity.AiSkillFile;
import org.bedrock.ai.enums.AiErrorEnum;
import org.bedrock.ai.enums.SkillNodeType;
import org.bedrock.ai.mapper.AiSkillMapper;
import org.bedrock.ai.param.AiSkillListParam;
import org.bedrock.ai.param.AiSkillSubmitParam;
import org.bedrock.ai.param.AiSkillSubmitParam.AiSkillFileItem;
import org.bedrock.ai.props.AiProperties;
import org.bedrock.ai.service.IAiSkillFileService;
import org.bedrock.ai.service.IAiSkillService;
import org.bedrock.ai.vo.AiSkillDetailVO;
import org.bedrock.ai.vo.AiSkillFileVO;
import org.bedrock.ai.vo.AiSkillListVO;
import org.bedrock.common.ai.skill.SkillManager;
import org.bedrock.common.ai.tool.ToolManager;
import org.bedrock.common.auth.util.AuthUtil;
import org.bedrock.common.boot.base.BaseController;
import org.bedrock.common.code.api.R;
import org.bedrock.common.code.util.*;
import org.bedrock.common.log.exception.ServiceException;
import org.bedrock.common.log.operation.support.LogRecordContext;
import org.bedrock.common.mybatisplus.base.BaseEntity;
import org.bedrock.common.mybatisplus.base.BaseServiceImpl;
import org.bedrock.common.mybatisplus.constant.BedrockDBConstant;
import org.bedrock.common.resource.model.oss.OssResultFile;
import org.bedrock.common.tenant.constant.TenantConstant;
import org.bedrock.common.tenant.handler.TenantHandler;
import org.bedrock.resource.dto.OssMultipartFile;
import org.bedrock.resource.feign.IOssClient;
import org.springaicommunity.agent.tools.AutoMemoryTools;
import org.springaicommunity.agent.tools.BraveWebSearchTool;
import org.springaicommunity.agent.tools.FileSystemTools;
import org.springaicommunity.agent.tools.GlobTool;
import org.springaicommunity.agent.tools.GrepTool;
import org.springaicommunity.agent.tools.ListDirectoryTool;
import org.springaicommunity.agent.tools.ShellTools;
import org.springaicommunity.agent.tools.SkillsTool;
import org.springaicommunity.agent.tools.TodoWriteTool;
import org.springaicommunity.agent.utils.MarkdownParser;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * AI Skill 服务。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiSkillServiceImpl extends BaseServiceImpl<AiSkillMapper, AiSkill>
        implements IAiSkillService, ApplicationRunner {

    private final AiProperties aiProperties;

    private final IAiSkillFileService aiSkillFileService;

    private final SkillManager skillManager;

    private final ToolManager toolManager;

    private final IOssClient iOssClient;

    /**
     * 新增 Skill。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submit(AiSkillSubmitParam param) {
        AiSkill aiSkill = BeanUtil.copyProperties(param, AiSkill.class);
        aiSkill.setStatus(BedrockDBConstant.DB_STATUS_DISABLE);
        fillSkillFromSkillMd(param, aiSkill);
        save(aiSkill);
        // 批量插入技能文件
        aiSkillFileService.saveBatch(flattenSkillFiles(param.getFiles(), aiSkill.getId()));
        return true;
    }

    /**
     * 修改 Skill。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean edit(AiSkillSubmitParam param) {
        AiSkill aiSkill = BeanUtil.copyProperties(param, AiSkill.class);
        AiSkill existing = getById(aiSkill.getId());
        fillSkillFromSkillMd(param, aiSkill);
        updateById(aiSkill);
        // 先删后插：全量替换文件树
        aiSkillFileService.removeBySkillId(aiSkill.getId());
        aiSkillFileService.saveBatch(flattenSkillFiles(param.getFiles(), aiSkill.getId()));
        // 仅启用态刷新本地目录与运行时注册
        if (Objects.equals(BedrockDBConstant.DB_STATUS_NORMAL, existing.getStatus())) {
            registerQuietly(aiSkill);
        }
        return true;
    }

    /**
     * 删除 Skill：逻辑删除主表，删除文件树与本地目录，并 unregister。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Long id) {
        AiSkill aiSkill = getById(id);
        if (aiSkill == null) {
            return true;
        }
        LogRecordContext.putVariable("name", aiSkill.getName());
        aiSkillFileService.removeBySkillId(id);
        boolean remove = logicRemoveById(id);
        unregisterQuietly(aiSkill.getId());
        return remove;
    }

    /**
     * 详情（含文件树；扁平列表组装为树）。
     */
    @Override
    public AiSkillDetailVO detail(Long id) {
        AiSkillDetailVO detailVO = baseMapper.selectDetailById(id);
        if (detailVO != null) {
            detailVO.setFiles(TreeUtil.buildTree(detailVO.getFiles()));
        }
        return detailVO;
    }

    /**
     * 无分页列表。
     */
    @Override
    public List<AiSkillListVO> selectAiSkillList(AiSkillListParam param) {
        return baseMapper.selectAiSkillList(null, param);
    }

    /**
     * 分页列表。
     */
    @Override
    public IPage<AiSkillListVO> selectAiSkillListPage(IPage<AiSkillListVO> iPage, AiSkillListParam param) {
        return iPage.setRecords(baseMapper.selectAiSkillList(iPage, param));
    }

    /**
     * 启用 / 禁用 Skill。
     */
    @Override
    public boolean enableStatus(Long id, Integer status) {
        AiSkill aiSkill = getById(id);
        if (aiSkill == null) {
            return true;
        }
        LogRecordContext.putVariable("name", aiSkill.getName());
        boolean updated = update(Wrappers.<AiSkill>lambdaUpdate()
                .eq(BaseEntity::getId, id)
                .set(BaseEntity::getUpdateTime, LocalDateTime.now())
                .set(BaseEntity::getUpdateUserId, AuthUtil.getUserId())
                .set(AiSkill::getStatus, status));
        if (!updated) {
            return false;
        }
        if (BedrockDBConstant.DB_STATUS_DISABLE.equals(status)) {
            unregisterQuietly(id);
            return true;
        }
        registerQuietly(aiSkill);
        return true;
    }

    /**
     * 解析上传文件为 Skill 文件节点项（不落库）。
     * <p>后缀命中文本列表时写 content；否则上传 OSS 写 fileUrl。</p>
     */
    @Override
    public AiSkillFileItem parseFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String suffix = FileUtil.getExtension(originalFilename).toLowerCase(Locale.ROOT);
        boolean text = isTextSuffix(suffix);
        String content = null;
        String fileUrl = null;
        if (text) {
            content = IoUtil.copyToString(file.getInputStream(), null);
        } else {
            R<OssResultFile> ossResultFileR = iOssClient.uploadFile(file);
            if (ossResultFileR.isSuccess()) {
                fileUrl = ossResultFileR.getData().url();
            }
        }
        return new AiSkillFileItem(
                SkillNodeType.FILE,
                originalFilename,
                text ? AiConstant.SKILL_FILE_IS_TEXT_YES : AiConstant.SKILL_FILE_IS_TEXT_NO,
                content,
                fileUrl,
                file.getSize(),
                0,
                null);
    }

    /**
     * 解析 Skill 压缩包为详情预览（含文件树，不落库）。
     * <p>文本写 content，二进制上传 OSS；从 SKILL.md frontmatter 解析 name/description。</p>
     */
    @Override
    public AiSkillDetailVO parseZip(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new ServiceException(AiErrorEnum.SKILL_ZIP_INVALID.getCode(),
                    AiErrorEnum.SKILL_ZIP_INVALID.getMessage());
        }
        String originalFilename = file.getOriginalFilename();
        String zipSuffix = FileUtil.getExtension(originalFilename).toLowerCase(Locale.ROOT);
        if (!"zip".equals(zipSuffix)) {
            throw new ServiceException(AiErrorEnum.SKILL_ZIP_INVALID);
        }
        List<AiSkillFileVO> rootNodes = buildFileTreeFromZip(file.getInputStream());
        if (rootNodes.isEmpty()) {
            throw new ServiceException(AiErrorEnum.SKILL_ZIP_INVALID);
        }
        AiSkillDetailVO detail = new AiSkillDetailVO();
        for (AiSkillFileVO node : rootNodes) {
            if (node.getName().equalsIgnoreCase("SKILL.md")) {
                MarkdownParser parser = new MarkdownParser(node.getContent());
                detail.setName(StringUtil.toStr(parser.getFrontMatter().get("name")));
                detail.setDescription(StringUtil.toStr(parser.getFrontMatter().get("description")));
                break;
            }
        }
        detail.setFiles(rootNodes);
        detail.setLabel(detail.getName());
        return detail;
    }

    /**
     * 启动时加载已启用 Skill 到 SkillManager。
     * <p>
     * 注入 load/unload 回调（registrationId = skill 主键）；{@link TenantHandler#ignore} 跨租户查出启用列表后注册。
     * 单条失败只打日志，避免拖垮启动；用户启停/编辑走 quietly 方法时异常仍向上抛出。
     * </p>
     */
    @Override
    public void loadEnabledOnStartup() {
        // 注册前落盘：按详情重建 {basePath}/{tenantId}/{name}
        skillManager.setLoadCallback((registrationId) -> {
            syncLocalSkillDir(registrationId);
            log.info("Loading Skill: {}", registrationId);
        });
        // 注销后删盘：按已移除 Skill 的 basePath
        skillManager.setUnloadCallback((registrationId, skill) -> {
            if (skill == null || StringUtil.isBlank(skill.basePath())) {
                log.warn("Skill basePath is blank: {}", registrationId);
                return;
            }
            FileUtil.deleteDir(Paths.get(skill.basePath()));
        });
        TenantHandler.ignore(() -> {
            List<AiSkill> enabledList = list(Wrappers.<AiSkill>lambdaQuery()
                    .eq(AiSkill::getStatus, BedrockDBConstant.DB_STATUS_NORMAL)
                    .eq(BaseEntity::getIsDeleted, BedrockDBConstant.DB_NOT_DELETED));
            for (AiSkill skill : enabledList) {
                try {
                    registerQuietly(skill);
                } catch (Exception e) {
                    log.error("Failed to register Skill [{}] id={}", skill.getName(), skill.getId(), e);
                }
            }
            log.info("Loaded {} enabled Skill(s) into SkillManager", enabledList.size());
        });
    }

    @Override
    public void run(ApplicationArguments args) {
        loadEnabledOnStartup();
        registerAgentUtilsTools();
    }

    /**
     * 将 spring-ai-agent-utils 可独立构建的工具注册到 {@link ToolManager}，供会话 tools 勾选。
     * <p>
     * 约束来自 {@code bedrock.ai.agent-utils}：{@code allowedDirectories} 空=不限制；
     * Brave / AutoMemory 仅配置非空时注册。
     * </p>
     */
    private void registerAgentUtilsTools() {
        AiProperties.AgentUtils agentUtils = aiProperties.getAgentUtils();
        List<String> allowedDirectories = agentUtils.getAllowedDirectories();
        List<String> registered = new ArrayList<>();
        FileSystemTools.Builder fileSystemBuilder = FileSystemTools.builder();
        allowedDirectories.forEach(fileSystemBuilder::allowedDirectory);
        registerToolGroup("fileSystem", "本地文件读写", fileSystemBuilder.build(), registered);
        ShellTools.Builder shellBuilder = ShellTools.builder();
        if (StringUtil.isNotBlank(agentUtils.getShellWorkingDirectory())) {
            shellBuilder.workingDirectory(agentUtils.getShellWorkingDirectory());
        }
        registerToolGroup("shell", "Shell 命令", shellBuilder.build(), registered);
        GrepTool.Builder grepBuilder = GrepTool.builder();
        allowedDirectories.forEach(grepBuilder::allowedDirectory);
        registerToolGroup("grep", "内容搜索", grepBuilder.build(), registered);
        GlobTool.Builder globBuilder = GlobTool.builder();
        allowedDirectories.forEach(globBuilder::allowedDirectory);
        registerToolGroup("glob", "文件名匹配", globBuilder.build(), registered);
        ListDirectoryTool.Builder listDirBuilder = ListDirectoryTool.builder();
        allowedDirectories.forEach(listDirBuilder::allowedDirectory);
        registerToolGroup("listDirectory", "目录列表", listDirBuilder.build(), registered);
        registerToolGroup("todoWrite", "任务清单", TodoWriteTool.builder().build(), registered);
        if (StringUtil.isNotBlank(agentUtils.getMemoriesDir())) {
            registerToolGroup("autoMemory", "持久记忆",
                    AutoMemoryTools.builder().memoriesDir(agentUtils.getMemoriesDir()).build(), registered);
        }
        if (StringUtil.isNotBlank(agentUtils.getBraveApiKey())) {
            registerToolGroup("braveWebSearch", "网页搜索",
                    BraveWebSearchTool.builder(agentUtils.getBraveApiKey()).build(), registered);
        }
        log.info("Registered agent-utils tools into ToolManager: {}", registered);
    }

    private void registerToolGroup(String name, String label, Object toolBean, List<String> registered) {
        List<ToolCallback> callbacks = List.of(ToolCallbacks.from(toolBean));
        toolManager.register(name, label, callbacks);
        registered.add(name);
    }

    /**
     * 按 Skill 详情流式打包下载 zip。
     * <ol>
     *   <li>查询扁平文件列表，不存在则抛 {@link AiErrorEnum#SKILL_NOT_FOUND}</li>
     *   <li>写入以 skill 名为名的包根 ZipEntry（{@code name/}）</li>
     *   <li>经 {@link BaseController.FileDownload#write} 边打边写：目录 entry 以 {@code /} 结尾；
     *       文本从 content 写入；非文本从 fileUrl 流式拷入</li>
     * </ol>
     */
    @Override
    public void downloadSkillZip(Long id, BaseController.FileDownload fileDownload) throws IOException {
        AiSkillDetailVO detail = baseMapper.selectDetailById(id);
        if (detail == null) {
            throw new ServiceException(AiErrorEnum.SKILL_NOT_FOUND);
        }
        String fileName = detail.getName().concat(".zip");
        // 流式写入 zip，避免整包缓冲
        fileDownload.write(fileName, out -> {
            try (ZipOutputStream zos = new ZipOutputStream(out, StandardCharsets.UTF_8)) {
                ZipEntry baseZipEntry = new ZipEntry(detail.getName().concat("/"));
                zos.putNextEntry(baseZipEntry);
                zos.closeEntry();
                for (AiSkillFileVO aiSkillFileVO : detail.getFiles()) {
                    String relativeName = StringUtil.isBlank(aiSkillFileVO.getRelativePath()) ?
                            detail.getName() : detail.getName()
                            .concat("/")
                            .concat(aiSkillFileVO.getRelativePath());
                    if (aiSkillFileVO.getNodeType() == SkillNodeType.DIRECTORY) {
                        relativeName += "/";
                    }
                    ZipEntry zipEntry = new ZipEntry(relativeName);
                    zos.putNextEntry(zipEntry);
                    if (aiSkillFileVO.getNodeType() == SkillNodeType.DIRECTORY) {
                        zos.closeEntry();
                        continue;
                    }
                    if (Objects.equals(aiSkillFileVO.getIsText(), AiConstant.SKILL_FILE_IS_TEXT_YES)) {
                        if (StringUtil.isNotBlank(aiSkillFileVO.getContent())) {
                            IoUtil.copy(aiSkillFileVO.getContent(), StandardCharsets.UTF_8, zos);
                        }
                        zos.closeEntry();
                        continue;
                    }
                    if (StringUtil.isBlank(aiSkillFileVO.getFileUrl())) {
                        zos.closeEntry();
                        continue;
                    }
                    Resource resource = ResourceUtil.getResource(aiSkillFileVO.getFileUrl());
                    try (InputStream inputStream = resource.getInputStream()) {
                        IoUtil.copy(inputStream, zos);
                    }
                    zos.closeEntry();

                }
                zos.finish();
            }
        });
    }

    /**
     * 从文件树根下 {@code SKILL.md} 解析并回填 {@code name}/{@code description}，并做合法性校验。
     * <ul>
     *   <li>缺少 SKILL.md → {@link AiErrorEnum#SKILL_ZIP_INVALID}</li>
     *   <li>name 为空 → {@link AiErrorEnum#SKILL_MD_INVALID}</li>
     *   <li>name 与未删除记录冲突（编辑时排除自身 id）→ {@link AiErrorEnum#SKILL_NAME_ALREADY_EXISTS}</li>
     * </ul>
     */
    private void fillSkillFromSkillMd(AiSkillSubmitParam param, AiSkill aiSkill) {
        AiSkillFileItem aiSkillFileItem = param.getFiles()
                .stream()
                .filter(pathEntry -> pathEntry.name().equalsIgnoreCase("SKILL.md"))
                .findFirst()
                .orElseThrow(() -> new ServiceException(AiErrorEnum.SKILL_ZIP_INVALID));
        SkillsTool.Skill skill = skillManager.parseContent(aiSkillFileItem.content());
        aiSkill.setName(skill.name());
        aiSkill.setDescription(StringUtil.toStr(skill.frontMatter().get("description")));
        if (StringUtil.isBlank(aiSkill.getName())) {
            throw new ServiceException(AiErrorEnum.SKILL_MD_INVALID);
        }
        if (exists(Wrappers.<AiSkill>lambdaQuery()
                .eq(AiSkill::getName, aiSkill.getName())
                .ne(ObjectUtil.isNotEmpty(aiSkill.getId()), AiSkill::getId, aiSkill.getId())
                .eq(BaseEntity::getIsDeleted, BedrockDBConstant.DB_NOT_DELETED))) {
            throw new ServiceException(AiErrorEnum.SKILL_NAME_ALREADY_EXISTS);
        }
    }

    /**
     * 将文件树扁平为待落库列表：生成 id、挂 skillId / parentId，并重建 relativePath。
     * <p>根节点 {@code parentId=0}、{@code relativePath=name}；子节点路径为 {@code 父.path + "/" + name}。</p>
     */
    private List<AiSkillFile> flattenSkillFiles(List<AiSkillFileItem> roots, Long skillId) {
        List<AiSkillFile> result = new ArrayList<>();
        // 队列元素：pair (当前节点, 父节点实体)；根的父为 null
        Queue<Map.Entry<AiSkillFileItem, AiSkillFile>> queue = new LinkedList<>();
        for (AiSkillFileItem root : roots) {
            queue.add(new AbstractMap.SimpleEntry<>(root, null));
        }
        while (!queue.isEmpty()) {
            Map.Entry<AiSkillFileItem, AiSkillFile> entry = queue.poll();
            AiSkillFileItem node = entry.getKey();
            AiSkillFile parentNode = entry.getValue();
            AiSkillFile skillFile = BeanUtil.copyProperties(node, AiSkillFile.class);
            skillFile.setId(IdWorker.getId());
            skillFile.setSkillId(skillId);
            if (skillFile.getNodeType() == SkillNodeType.FILE) {
                skillFile.setFileSuffix(FileUtil.getExtension(node.name()).toLowerCase(Locale.ROOT));
            }
            if (parentNode != null) {
                skillFile.setParentId(parentNode.getId());
                skillFile.setRelativePath(parentNode.getRelativePath() + "/" + skillFile.getName());
            } else {
                skillFile.setParentId(0L);
                skillFile.setRelativePath(skillFile.getName());
            }
            result.add(skillFile);
            if (CollectionUtil.isNotEmpty(node.children())) {
                for (AiSkillFileItem child : node.children()) {
                    queue.add(new AbstractMap.SimpleEntry<>(child, skillFile));
                }
            }
        }
        return result;
    }

    /**
     * 从 Skill zip 构建文件树（不落库）。
     * <ol>
     *   <li>{@link #collectZipPathEntries} 遍历 zip，收集路径与内容</li>
     *   <li>{@link #ensureMissingParentDirs} 补全缺失的父目录条目</li>
     *   <li>{@link #mapEntriesToFileNodes} 定位 SKILL.md、剥离 basePath，映射为 VO（文本 / OSS）</li>
     *   <li>{@link #assembleFileTree} 按相对路径挂 children，返回根节点列表</li>
     * </ol>
     */
    private List<AiSkillFileVO> buildFileTreeFromZip(InputStream inputStream) throws IOException {
        // 1. 读取zip + 路径清洗
        List<ZipPathEntry> pathEntries = collectZipPathEntries(inputStream);
        // 2. 补齐缺失中间目录
        ensureMissingParentDirs(pathEntries);
        // 3. 基于SKILL.md剥离basePath，转VO、文本解析、OSS上传
        List<AiSkillFileVO> nodes = mapEntriesToFileNodes(pathEntries);
        // 4. 组装树，返回根节点列表
        return assembleFileTree(nodes);
    }

    /**
     * 遍历 zip，收集路径与内容。
     * <p>路径统一为正斜杠并压缩连续 {@code /}；目录条目内容为空，文件读入字节数组。</p>
     */
    private List<ZipPathEntry> collectZipPathEntries(InputStream inputStream) throws IOException {
        List<ZipPathEntry> pathEntries = new ArrayList<>();
        try (ZipInputStream zipIn = new ZipInputStream(inputStream, StandardCharsets.UTF_8)) {
            ZipEntry zipEntry;
            while ((zipEntry = zipIn.getNextEntry()) != null) {
                String rawName = zipEntry.getName();
                if (rawName.isBlank()) {
                    continue;
                }
                // 统一为正斜杠，并将连续 / 压成单个 /
                rawName = rawName.replace("\\", "/")
                        .replaceAll("/+", "/");
                String name = FileUtil.getFileName(rawName);
                if (StringUtil.isBlank(name)) {
                    continue;
                }
                boolean directory = zipEntry.isDirectory();
                byte[] bytes = directory ? IoUtil.EMPTY_CONTENT : IoUtil.copyToByteArray(zipIn);
                pathEntries.add(new ZipPathEntry(rawName, name, directory, bytes));
                zipIn.closeEntry();
            }
        }
        return pathEntries;
    }

    /**
     * 补全 zip 中未单独出现的父目录条目（部分 zip 只有文件路径、不列出目录）。
     * <p>对每个条目向上逐层提取父路径（保留结尾 {@code /}），去重后追加为目录节点，避免组树时找不到 parent。</p>
     */
    private void ensureMissingParentDirs(List<ZipPathEntry> pathEntries) {
        Set<String> existPathSet = pathEntries.stream().map(ZipPathEntry::relativePath).collect(Collectors.toSet());
        // LinkedHashSet：去重并尽量保持发现顺序
        Set<String> newParents = new LinkedHashSet<>();
        for (ZipPathEntry entry : pathEntries) {
            String p = entry.relativePath();
            while (true) {
                int idx = p.lastIndexOf('/', p.endsWith("/") ? p.length() - 2 : p.length() - 1);
                if (idx <= 0) {
                    break; // 没有父目录或已到根
                }
                String parent = p.substring(0, idx + 1); // 保留结尾 "/"
                if (!existPathSet.contains(parent)) {
                    existPathSet.add(parent); // 避免同一路径重复收集
                    newParents.add(parent);
                }
                p = parent;
            }
        }
        for (String newParent : newParents) {
            String fileName = FileUtil.getFileName(newParent);
            pathEntries.add(new ZipPathEntry(newParent, fileName));
        }
    }

    /**
     * 定位 SKILL.md，以其目录为 basePath 剥离包外层前缀，并映射为 {@link AiSkillFileVO}。
     * <ul>
     *   <li>无 SKILL.md 时抛 {@link AiErrorEnum#SKILL_ZIP_INVALID}；多个时取第一个</li>
     *   <li>目录：{@link SkillNodeType#DIRECTORY}，isText=NO</li>
     *   <li>文本后缀：写 content（沿用 VO 默认 FILE / isText=YES）</li>
     *   <li>非文本：上传 OSS 写 fileUrl，isText=NO</li>
     *   <li>剥离后 relativePath 为空的节点（包根目录自身）丢弃</li>
     * </ul>
     */
    private List<AiSkillFileVO> mapEntriesToFileNodes(List<ZipPathEntry> pathEntries) {
        ZipPathEntry skillMd = pathEntries.stream()
                .filter(pathEntry -> pathEntry.name().equalsIgnoreCase("SKILL.md"))
                .findFirst()
                .orElseThrow(() -> new ServiceException(AiErrorEnum.SKILL_ZIP_INVALID));
        String skillPath = skillMd.relativePath();
        String basePath = skillPath.substring(0, skillPath.length() - skillMd.name().length());
        return pathEntries.stream()
                .filter(pathEntry -> pathEntry.relativePath().startsWith(basePath))
                .map(pathEntry -> {
                    AiSkillFileVO fileNode = new AiSkillFileVO();
                    String relativePath = pathEntry.relativePath();
                    if (StringUtil.isNotBlank(basePath) && relativePath.startsWith(basePath)) {
                        relativePath = relativePath.substring(basePath.length());
                    }
                    fileNode.setRelativePath(relativePath);
                    fileNode.setName(pathEntry.name());
                    fileNode.setFileSize((long) pathEntry.bytes().length);
                    fileNode.setFileSuffix(FileUtil.getExtension(pathEntry.name()).toLowerCase(Locale.ROOT));
                    if (pathEntry.directory()) {
                        fileNode.setNodeType(SkillNodeType.DIRECTORY);
                        fileNode.setIsText(AiConstant.SKILL_FILE_IS_TEXT_NO);
                    } else if (isTextSuffix(fileNode.getFileSuffix())) {
                        // nodeType / isText 使用 AiSkillFileVO 默认值
                        fileNode.setContent(new String(pathEntry.bytes(), StandardCharsets.UTF_8));
                    } else {
                        R<OssResultFile> uploadResult = iOssClient.uploadFile(
                                OssMultipartFile.of(pathEntry.bytes(), pathEntry.name()));
                        if (uploadResult.isSuccess()) {
                            fileNode.setFileUrl(uploadResult.getData().url());
                        }
                        fileNode.setIsText(AiConstant.SKILL_FILE_IS_TEXT_NO);
                    }
                    return fileNode;
                })
                .filter(fileNode -> StringUtil.hasText(fileNode.getRelativePath()))
                .toList();
    }

    /**
     * 按相对路径挂到父节点 {@code children}，返回根节点列表。
     * <p>组树后清空节点上的 relativePath（落库时再重建）；挂不到父节点或已在包根下的节点
     * {@code parentId=0}；{@code SKILL.md} 排序为 0。</p>
     */
    private List<AiSkillFileVO> assembleFileTree(List<AiSkillFileVO> nodes) {
        Map<String, AiSkillFileVO> nodeByPath = nodes.stream()
                .collect(Collectors.toMap(AiSkillFileVO::getRelativePath, Function.identity()));
        int sort = 1;
        for (AiSkillFileVO node : nodes) {
            String key = node.getName();
            String relativePath = node.getRelativePath();
            node.setRelativePath(null);
            if (node.getNodeType() == SkillNodeType.DIRECTORY) {
                key = key.concat("/");
            }
            if (!relativePath.equals(key)) {
                int idx = relativePath.lastIndexOf('/', relativePath.endsWith("/") ? relativePath.length() - 2 : relativePath.length() - 1);
                key = relativePath.substring(0, idx + 1); // 保留结尾 "/"
                AiSkillFileVO parent = nodeByPath.get(key);
                if (parent != null) {
                    List<AiSkillFileVO> children = parent.getChildren();
                    node.setSort(children.size());
                    children.add(node);
                } else {
                    node.setParentId(0L);
                    node.setSort(sort++);
                }
            } else if (node.getName().equalsIgnoreCase("SKILL.md")) {
                node.setParentId(0L);
                node.setSort(0);
            } else {
                node.setParentId(0L);
                node.setSort(sort++);
            }
        }
        return nodes.stream()
                .filter(node -> Objects.equals(node.getParentId(), 0L))
                .sorted()
                .toList();
    }

    /**
     * 注销 SkillManager 注册项；删盘由 unloadCallback 按 basePath 处理。
     * <p>启停/删除场景异常向上抛出；启动批量加载请在外层捕获。
     */
    private void unregisterQuietly(Long skillId) {
        skillManager.unregister(StringUtil.toStr(skillId));
    }

    /**
     * 按 skill 主键注册：{@code register(id, path)}，由 Manager 先 unload 再
     * {@link #syncLocalSkillDir} 再 loadDirectory。
     * <p>启停/编辑场景异常向上抛出；启动批量加载请在外层捕获。
     */
    private void registerQuietly(AiSkill skill) {
        skillManager.register(StringUtil.toStr(skill.getId()), getSkillRoot(skill.getTenantId(), skill.getName()));
    }

    /**
     * 仅作 {@link SkillManager.SkillLoadCallback}：按详情重建本地目录。
     * <p>
     * 路径为 {@code {basePath}/{tenantId}/{name}}；先 {@code deleteDir} 再写，
     * 文本写 content，非文本从 fileUrl 拉取。与 unload 删旧 basePath 互补，避免残留。
     * </p>
     */
    private void syncLocalSkillDir(String skillId) {
        AiSkillDetailVO detail = baseMapper.selectDetailById(NumberUtil.toLong(skillId));
        if (detail == null) {
            return;
        }
        Path skillRoot = getSkillRoot(detail.getTenantId(), detail.getName());
        FileUtil.deleteDir(skillRoot);
        FileUtil.mkdirs(skillRoot);
        if (CollectionUtil.isEmpty(detail.getFiles())) {
            return;
        }
        try {
            for (AiSkillFileVO file : detail.getFiles()) {
                Path filePath = skillRoot.resolve(file.getRelativePath());
                if (file.getNodeType() == SkillNodeType.DIRECTORY) {
                    FileUtil.mkdirs(filePath);
                    continue;
                }
                if (Objects.equals(file.getIsText(), AiConstant.SKILL_FILE_IS_TEXT_YES)) {
                    FileUtil.writeString(filePath, file.getContent());
                    continue;
                }
                if (StringUtil.isBlank(file.getFileUrl())) {
                    continue;
                }
                Resource resource = ResourceUtil.getResource(file.getFileUrl());
                try (InputStream in = resource.getInputStream()) {
                    FileUtil.toFile(in, filePath);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }

    /**
     * 本地技能根目录：{@code {basePath}/{tenantId}/{name}}。
     * <p>tenantId 为空时依次回落 {@link AuthUtil#getTenantId()}、{@link TenantConstant#DEFAULT_TENANT_ID}。
     */
    private Path getSkillRoot(String tenantId, String name) {
        if (StringUtil.isBlank(tenantId)) {
            tenantId = AuthUtil.getTenantId();
        }
        if (StringUtil.isBlank(tenantId)) {
            tenantId = TenantConstant.DEFAULT_TENANT_ID;
        }
        return Paths.get(aiProperties.getSkill().getBasePath(), tenantId, name);
    }

    private boolean isTextSuffix(String suffix) {
        return StringUtil.isBlank(suffix)
                || ObjectUtil.isEmpty(aiProperties.getSkill().getTextFileSuffixes())
                || aiProperties.getSkill().getTextFileSuffixes().contains(suffix.toLowerCase());
    }

    /**
     * 压缩包路径条目。
     */
    private record ZipPathEntry(String relativePath, String name, boolean directory, byte[] bytes) {

        public ZipPathEntry(String relativePath, String name) {
            this(relativePath, name, true, IoUtil.EMPTY_CONTENT);
        }

    }

}
