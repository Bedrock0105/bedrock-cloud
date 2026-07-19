package org.bedrock.system.tasks;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bedrock.system.service.IAdminOnlineService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AdminOnlineScheduled {

    private final IAdminOnlineService iAdminOnlineService;

    /**
     * 定时清理空连接条目（每天夜里12点执行）
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Async
    public void cleanEmptyEntries() {
        try {
            iAdminOnlineService.deleteAdminOnline(60 * 60 * 24 * 7);
        } catch (Exception e) {
            log.error("定时清理空连接条目失败", e);
        }
    }
}
