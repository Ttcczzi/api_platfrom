package com.wt.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * @author xcx
 * @date
 */
@Configuration
@EnableScheduling
public class TastScheduleConfig implements SchedulingConfigurer {
    ThreadPoolTaskScheduler taskScheduler =  new ThreadPoolTaskScheduler();

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskScheduler.setBeanName("taskScheduler");
        taskScheduler.initialize();
        taskRegistrar.setTaskScheduler(taskScheduler);
    }
}
