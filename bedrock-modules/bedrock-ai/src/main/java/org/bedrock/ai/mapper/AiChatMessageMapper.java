package org.bedrock.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.bedrock.ai.entity.AiChatMessage;
import org.bedrock.ai.vo.AiChatMessageListVO;

import java.util.List;

/**
 * AI 聊天消息明细 Mapper
 */
public interface AiChatMessageMapper extends BaseMapper<AiChatMessage> {

    /**
     * 按会话 id 查询历史消息列表（前端回显，按时间升序）
     */
    List<AiChatMessageListVO> selectMessageListByRecordId(@Param("recordId") Long recordId);

    /**
     * 加载 Advisor 上下文所需的历史消息（已完成消息，按时间升序，最多 maxMessages 条）
     */
    List<AiChatMessage> selectHistoryMessages(@Param("recordId") Long recordId,
                                              @Param("maxMessages") int maxMessages);

}
