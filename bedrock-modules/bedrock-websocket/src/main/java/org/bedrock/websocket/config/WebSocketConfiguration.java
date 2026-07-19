package org.bedrock.websocket.config;

import lombok.extern.slf4j.Slf4j;
import org.bedrock.websocket.handle.IModelOperateHandle;
import org.bedrock.websocket.manage.ModelOperateManage;
import org.bedrock.websocket.manage.WebSocketManage;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Configuration
public class WebSocketConfiguration {

    /**
     * 创建模型处理类
     */
    @Bean("modelOperateManage")
    public ModelOperateManage modelOperateManage(ObjectProvider<List<IModelOperateHandle>> modelOperateHandle) {
        return new ModelOperateManage(modelOperateHandle.getIfAvailable(Collections::emptyList));
    }

    /**
     * 创建websocket管理类
     */
    @Bean("webSocketManage")
    public WebSocketManage webSocketManage(@Qualifier("modelOperateManage") ModelOperateManage modelOperateManage) {
        return new WebSocketManage(modelOperateManage);
    }

    @Bean("webSocketExecutor")
    public Executor webSocketExecutor() {
        ThreadPoolTaskExecutor webSocketEx = new ThreadPoolTaskExecutor();
        // 核心线程数：线程池创建时候初始化的线程数
        webSocketEx.setCorePoolSize(10);
        // 最大线程数：线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        webSocketEx.setMaxPoolSize(20);
        // 缓冲队列：用来缓冲执行任务的队列
        webSocketEx.setQueueCapacity(1500);
        // 允许线程的空闲时间60秒：当超过了核心线程之外的线程在空闲时间到达之后会被销毁
        webSocketEx.setKeepAliveSeconds(60);
        // 线程池名的前缀：设置好了之后可以方便我们定位处理任务所在的线程池
        webSocketEx.setThreadNamePrefix("webSocketEx--");
        // 缓冲队列满了之后的拒绝策略：由调用线程处理（一般是主线程）
        webSocketEx.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        //调度器shutdown被调用时等待当前被调度的任务完成
        webSocketEx.setWaitForTasksToCompleteOnShutdown(true);
        //等待时长
        webSocketEx.setAwaitTerminationSeconds(60);
        webSocketEx.initialize();
        return webSocketEx;
    }
}
