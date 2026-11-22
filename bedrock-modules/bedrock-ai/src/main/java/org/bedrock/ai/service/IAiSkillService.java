package org.bedrock.ai.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.bedrock.ai.entity.AiSkill;
import org.bedrock.ai.param.AiSkillListParam;
import org.bedrock.ai.param.AiSkillSubmitParam;
import org.bedrock.ai.vo.AiSkillDetailVO;
import org.bedrock.ai.vo.AiSkillListVO;
import org.bedrock.common.boot.base.BaseController;
import org.bedrock.common.mybatisplus.base.IBaseService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IAiSkillService extends IBaseService<AiSkill> {

    /**
     * 添加 Skill（默认禁用；本地目录延后到启用时同步）
     */
    boolean submit(AiSkillSubmitParam param);

    /**
     * 修改 Skill（name 来自 SKILL.md，可变更；仅原启用态同步本地目录并重新注册）
     */
    boolean edit(AiSkillSubmitParam param);

    /**
     * 删除 Skill（逻辑删除，并处理文件树、本地目录与 unregister）
     */
    boolean removeById(Long id);

    /**
     * 详情（含文件树）
     */
    AiSkillDetailVO detail(Long id);

    /**
     * 无分页列表
     */
    List<AiSkillListVO> selectAiSkillList(AiSkillListParam param);

    /**
     * 分页列表
     */
    IPage<AiSkillListVO> selectAiSkillListPage(IPage<AiSkillListVO> iPage, AiSkillListParam param);

    /**
     * 启用 / 禁用（启用前同步本地目录再 register；禁用 unregister）
     */
    boolean enableStatus(Long id, Integer status);

    /**
     * 解析上传文件为 Skill 文件节点项（不落库）。
     * <p>
     * 后缀命中文本列表时写入 {@code content}；否则上传 OSS 写入 {@code fileUrl}。
     * </p>
     */
    AiSkillSubmitParam.AiSkillFileItem parseFile(MultipartFile file) throws IOException;

    /**
     * 解析 Skill 压缩包为详情预览（含文件树，不落库）。
     * <p>
     * 文本写入 {@code content}，二进制上传 OSS 写 {@code fileUrl}；
     * 从根目录 {@code SKILL.md} frontmatter 解析 {@code name}/{@code description}。
     * </p>
     */
    AiSkillDetailVO parseZip(MultipartFile file) throws IOException;

    /**
     * 流式下载 Skill 压缩包（扁平文件列表打包，含包根目录 entry）。
     */
    void downloadSkillZip(Long id, BaseController.FileDownload fileDownload) throws IOException;

    /**
     * 启动时加载已启用 Skill 到本地目录并注册 SkillManager
     */
    void loadEnabledOnStartup();

}
