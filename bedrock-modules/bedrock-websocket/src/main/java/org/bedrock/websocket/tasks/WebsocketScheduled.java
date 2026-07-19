package org.bedrock.websocket.tasks;

import lombok.extern.slf4j.Slf4j;
import org.bedrock.websocket.manage.WebSocketManage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WebsocketScheduled {

    /**
     * 定时清理空连接条目（每小时执行）
     */
    @Scheduled(cron = "0 0 */1 * * ?")
    @Async("webSocketExecutor")
    public void cleanEmptyEntries() {
        WebSocketManage webSocketManage = WebSocketManage.getWebSocketManage();
        if (webSocketManage != null) {
            try {
                webSocketManage.cleanEmptyEntries();
            } catch (Exception e) {
                log.error("定时清理空连接条目失败", e);
            }
        }
    }
}
