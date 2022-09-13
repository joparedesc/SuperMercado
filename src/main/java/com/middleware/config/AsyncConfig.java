package com.middleware.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * AsyncConfig class.
 * @author jesu_
 */
@Configuration
public class AsyncConfig {

    @Value("${async-config.core-pool-size}")
    private int corePoolSize;

    @Value("${async-config.max-pool-size}")
    private int maxPoolSize;

    @Value("${async-config.queue-capacity}")
    private int queueCapacity;

    @Value("${async-config.thread-name}")
    private String threadName;


    @Bean("threadPoolExecutor")
    public TaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadName);
        executor.initialize();
        return executor;
    }
}
