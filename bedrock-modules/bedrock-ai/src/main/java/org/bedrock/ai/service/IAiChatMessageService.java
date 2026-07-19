package org.bedrock.ai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.bedrock.ai.entity.AiChatMessage;
import org.bedrock.ai.vo.AiChatMessageListVO;

import java.util.List;

/**
 * AI 聊天消息明细 Service
 */
public interface IAiChatMessageService extends IService<AiChatMessage> {

    /**
     * 按会话 id 查询历史消息列表（校验会话归属当前用户）
     *
     * @param recordId 会话 id
     * @return 按时间升序的消息列表
     */
    List<AiChatMessageListVO> selectMessageListByRecordId(Long recordId);


}
