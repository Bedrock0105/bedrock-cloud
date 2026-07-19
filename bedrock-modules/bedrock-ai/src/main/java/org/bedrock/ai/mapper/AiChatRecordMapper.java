package org.bedrock.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.bedrock.ai.entity.AiChatRecord;
import org.bedrock.ai.enums.AiChatTypeEnum;
import org.bedrock.ai.param.AiChatRecordAdminListParam;
import org.bedrock.ai.vo.AiChatRecordAdminDetailVO;
import org.bedrock.ai.vo.AiChatRecordAdminListVO;
import org.bedrock.ai.vo.AiChatRecordDetailVO;
import org.bedrock.ai.vo.AiChatRecordListVO;

import java.time.LocalDateTime;
import java.util.List;

public interface AiChatRecordMapper extends BaseMapper<AiChatRecord> {

    /**
     * 根据ID查询详情（用户端）
     */
    AiChatRecordDetailVO selectDetailById(@Param("id") Long id);

    /**
     * 根据ID查询详情（管理端）
     */
    AiChatRecordAdminDetailVO selectAdminDetailById(@Param("id") Long id);

    /**
     * 查询当前用户会话列表
     *
     * @param chatType 会话类型，为空时默认仅查 CHAT
     */
    List<AiChatRecordListVO> selectChatRecordList(@Param("userId") Long userId,
                                                  @Param("chatType") AiChatTypeEnum chatType);

    /**
     * 管理端：租户内全员会话分页
     */
    List<AiChatRecordAdminListVO> selectAdminChatRecordListPage(IPage<AiChatRecordAdminListVO> iPage,
                                                                @Param("param") AiChatRecordAdminListParam param);

    /**
     * 根据ID更新令牌使用情况
     */
    int updateTokenUsageById(@Param("recordId") Long recordId,
                             @Param("promptTokens") Integer promptTokens,
                             @Param("completionTokens") Integer completionTokens,
                             @Param("totalTokens") Integer totalTokens,
                             @Param("updateTime") LocalDateTime updateTime);
}
