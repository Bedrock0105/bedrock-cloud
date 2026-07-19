package org.bedrock.ai.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.bedrock.ai.dto.AiChatOptions;
import org.bedrock.ai.entity.AiChatRecord;
import org.bedrock.ai.enums.AiChatTypeEnum;
import org.bedrock.ai.param.AiChatRecordAdminListParam;
import org.bedrock.ai.param.AiChatRecordEditParam;
import org.bedrock.ai.vo.*;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * AI 聊天会话服务接口。
 * <p>负责会话的增删改查与生命周期管理。</p>
 */
public interface IAiChatRecordService extends IService<AiChatRecord> {

    /**
     * 查询当前用户会话列表。
     *
     * @param chatType 会话类型，为空时默认 {@link AiChatTypeEnum#CHAT}
     */
    List<AiChatRecordListVO> selectChatRecordList(@Nullable AiChatTypeEnum chatType);

    /**
     * 管理端：租户内全员会话分页。
     */
    IPage<AiChatRecordAdminListVO> selectAdminChatRecordListPage(IPage<AiChatRecordAdminListVO> iPage,
                                                                 AiChatRecordAdminListParam param);

    /**
     * 查询会话详情（用户端）。
     *
     * @param id 会话 id
     * @return 会话详情；不存在时返回 {@code null}
     */
    AiChatRecordDetailVO detail(Long id);

    /**
     * 查询会话详情（管理端）。
     *
     * @param id 会话 id
     * @return 管理端会话详情；不存在时返回 {@code null}
     */
    AiChatRecordAdminDetailVO adminDetail(Long id);

    /**
     * 编辑会话（标题、系统提示词、{@link AiChatOptions} 等）。
     *
     * @param param 编辑参数
     * @return 是否更新成功
     */
    boolean edit(AiChatRecordEditParam param);

    /**
     * 删除会话（逻辑删除）。
     *
     * @param id 会话 id
     * @return 是否删除成功
     */
    boolean removeChatRecord(Long id);

    /**
     * 置顶 / 取消置顶。
     * <p>置顶时会取消当前用户其他会话的置顶状态。</p>
     *
     * @param id    会话 id
     * @param isTop 是否置顶：{@code 1} 置顶，{@code 0} 取消
     * @return 是否更新成功
     */
    boolean top(Long id, Integer isTop);

    /**
     * 基于角色创建会话。
     * <p>从角色同步系统提示词、工具与知识库检索配置，并合并模型默认参数。</p>
     *
     * @param roleId 角色 id
     * @return 新建会话详情
     */
    AiChatRecordDetailVO createFromRole(Long roleId);

    /**
     * 初始化并保存新会话。
     * <p>
     * {@code chatOptions} 为空时使用模型默认配置；
     * 非空时会与模型默认值合并（温度、Token、消息数上限等以会话配置优先，未设置项回填模型默认）。
     * </p>
     *
     * @param detailVO     模型详情
     * @param title        会话标题
     * @param systemPrompt 系统提示词
     * @param roleId       角色 id，可为 {@code null}
     * @param chatOptions  对话调用配置，可为 {@code null}
     * @return 新建会话详情
     */
    AiChatRecordDetailVO createAndSaveChatRecord(AiModelDetailVO detailVO, String title, String systemPrompt,
                                                 @Nullable Long roleId, @Nullable AiChatOptions chatOptions,
                                                 AiChatTypeEnum chatType);

    /**
     * 更新会话绑定的模型，并失效会话详情缓存。
     *
     * @param recordId 会话 id
     * @param modelId  模型 id
     */
    void changeChatRecordModel(Long recordId, Long modelId);

    /**
     * 累加更新会话 Token 用量统计。
     *
     * @param recordId 会话 id
     * @param usage    本次模型调用用量
     */
    void updateTokenUsage(Long recordId, Usage usage);

}
