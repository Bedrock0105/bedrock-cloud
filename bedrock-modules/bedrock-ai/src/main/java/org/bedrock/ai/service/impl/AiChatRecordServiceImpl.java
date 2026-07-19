package org.bedrock.ai.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.bedrock.ai.cache.AiCache;
import org.bedrock.ai.dto.AiChatOptions;
import org.bedrock.ai.entity.AiChatRecord;
import org.bedrock.ai.enums.AiChatTypeEnum;
import org.bedrock.ai.enums.AiErrorEnum;
import org.bedrock.ai.mapper.AiChatRecordMapper;
import org.bedrock.ai.param.AiChatRecordAdminListParam;
import org.bedrock.ai.param.AiChatRecordEditParam;
import org.bedrock.ai.service.IAiChatRecordService;
import org.bedrock.ai.service.IAiModelService;
import org.bedrock.ai.service.IAiRoleService;
import org.bedrock.ai.vo.*;
import org.bedrock.common.auth.util.AuthUtil;
import org.bedrock.common.code.constant.CacheConstant;
import org.bedrock.common.code.util.*;
import org.bedrock.common.log.exception.ServiceException;
import org.bedrock.common.mybatisplus.base.BaseEntity;
import org.bedrock.common.mybatisplus.constant.BedrockDBConstant;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * AI 聊天会话服务实现。
 * <p>负责会话的增删改查与生命周期管理；详情查询走缓存，写操作同步失效缓存。</p>
 */
@Service
@RequiredArgsConstructor
public class AiChatRecordServiceImpl extends ServiceImpl<AiChatRecordMapper, AiChatRecord> implements IAiChatRecordService {

    private final IAiModelService aiModelService;

    private final IAiRoleService aiRoleService;

    private IAiChatRecordService self;

    /**
     * 查询当前用户会话列表。
     */
    @Override
    public List<AiChatRecordListVO> selectChatRecordList(AiChatTypeEnum chatType) {
        AiChatTypeEnum type = chatType == null ? AiChatTypeEnum.CHAT : chatType;
        return baseMapper.selectChatRecordList(AuthUtil.getUserId(), type);
    }

    /**
     * 管理端：租户内全员会话分页。
     */
    @Override
    public IPage<AiChatRecordAdminListVO> selectAdminChatRecordListPage(IPage<AiChatRecordAdminListVO> iPage,
                                                                        AiChatRecordAdminListParam param) {
        return iPage.setRecords(baseMapper.selectAdminChatRecordListPage(iPage, param));
    }

    /**
     * 查询会话详情（结果缓存）。
     */
    @Override
    @Cacheable(cacheNames = CacheConstant.AI_CACHE, key = "'" + AiCache.CHAT_RECORD_DETAIL_ID + "' + #id")
    public AiChatRecordDetailVO detail(Long id) {
        return baseMapper.selectDetailById(id);
    }

    /**
     * 管理端会话详情（不走用户端缓存）。
     */
    @Override
    public AiChatRecordAdminDetailVO adminDetail(Long id) {
        return baseMapper.selectAdminDetailById(id);
    }

    /**
     * 编辑会话，并失效详情缓存。
     */
    @Override
    @CacheEvict(cacheNames = CacheConstant.AI_CACHE, key = "'" + AiCache.CHAT_RECORD_DETAIL_ID + "' + #param.id")
    public boolean edit(AiChatRecordEditParam param) {
        return this.update(Wrappers.<AiChatRecord>lambdaUpdate()
                .eq(BaseEntity::getId, param.getId())
                .set(StringUtil.isNotBlank(param.getTitle()), AiChatRecord::getTitle, param.getTitle())
                .set(param.getSystemPrompt() != null, AiChatRecord::getSystemPrompt, param.getSystemPrompt())
                .set(param.getChatOptions() != null, AiChatRecord::getChatOptions, JsonUtil.toJson(param.getChatOptions())));
    }

    /**
     * 逻辑删除会话，并失效详情缓存。
     */
    @Override
    @CacheEvict(cacheNames = CacheConstant.AI_CACHE, key = "'" + AiCache.CHAT_RECORD_DETAIL_ID + "' + #id")
    public boolean removeChatRecord(Long id) {
        return this.update(Wrappers.<AiChatRecord>lambdaUpdate()
                .eq(BaseEntity::getId, id)
                .set(BaseEntity::getIsDeleted, BedrockDBConstant.DB_IS_DELETED));
    }

    /**
     * 置顶 / 取消置顶；置顶时会取消当前用户其他会话的置顶状态。
     */
    @Override
    @CacheEvict(cacheNames = CacheConstant.AI_CACHE, key = "'" + AiCache.CHAT_RECORD_DETAIL_ID + "' + #id")
    public boolean top(Long id, Integer isTop) {
        if (isTop == BedrockDBConstant.DB_STATUS_NORMAL) {
            this.update(Wrappers.<AiChatRecord>lambdaUpdate()
                    .ne(AiChatRecord::getId, id)
                    .eq(AiChatRecord::getUserId, AuthUtil.getUserId())
                    .set(AiChatRecord::getIsTop, BedrockDBConstant.DB_STATUS_DISABLE));
        }
        return this.update(Wrappers.<AiChatRecord>lambdaUpdate()
                .eq(BaseEntity::getId, id)
                .set(AiChatRecord::getIsTop, isTop));
    }

    /**
     * 基于角色创建会话：同步角色提示词与 {@link AiChatOptions}，交由 {@link #createAndSaveChatRecord} 合并模型默认参数后入库。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiChatRecordDetailVO createFromRole(Long roleId) {
        AiRoleDetailVO roleDetail = aiRoleService.detail(roleId);
        if (roleDetail == null) {
            throw new ServiceException(AiErrorEnum.ROLE_NOT_FOUND.getCode(), AiErrorEnum.ROLE_NOT_FOUND.getMessage());
        }
        AiModelDetailVO detail = aiModelService.detail(roleDetail.getModelId());
        AiChatOptions chatOptions = AiChatOptions.fromRole(roleDetail);
        return createAndSaveChatRecord(detail, roleDetail.getRoleName(), roleDetail.getSystemPrompt(),
                roleDetail.getId(), chatOptions, AiChatTypeEnum.CHAT);
    }

    @Override
    public AiChatRecordDetailVO createAndSaveChatRecord(AiModelDetailVO detailVO, String title, String systemPrompt,
                                                        Long roleId, AiChatOptions chatOptions,
                                                        AiChatTypeEnum chatType) {
        AiChatOptions options = chatOptions == null ? AiChatOptions.fromModel(detailVO)
                : chatOptions.mergeModelDefaults(detailVO);
        AiChatRecord chatRecord = new AiChatRecord();
        chatRecord.setUserId(AuthUtil.getUserId());
        chatRecord.setModelId(detailVO.getId());
        chatRecord.setRoleId(roleId);
        chatRecord.setTitle(title);
        chatRecord.setSystemPrompt(systemPrompt);
        chatRecord.setChatOptions(options);
        chatRecord.setIsTop(BedrockDBConstant.DB_STATUS_DISABLE);
        chatRecord.setChatType(chatType);
        this.save(chatRecord);
        return getSelf().detail(chatRecord.getId());
    }

    /**
     * 更新会话绑定的模型，并手动失效详情缓存。
     */
    @Override
    public void changeChatRecordModel(Long recordId, Long modelId) {
        this.update(Wrappers.<AiChatRecord>lambdaUpdate()
                .eq(BaseEntity::getId, recordId)
                .set(AiChatRecord::getModelId, modelId));
        CacheUtil.evict(CacheConstant.AI_CACHE, AiCache.CHAT_RECORD_DETAIL_ID + recordId);
    }

    /**
     * 累加更新会话 Token 用量统计。
     */
    @Override
    public void updateTokenUsage(Long recordId, Usage usage) {
        baseMapper.updateTokenUsageById(recordId, usage.getPromptTokens(), usage.getCompletionTokens(), usage.getTotalTokens(), LocalDateTime.now());
    }

    /**
     * 获取当前 Service 代理，避免同类内部调用导致缓存注解失效。
     */
    public IAiChatRecordService getSelf() {
        if (self == null) {
            self = SpringUtil.getBean(IAiChatRecordService.class);
        }
        return self;
    }

}
