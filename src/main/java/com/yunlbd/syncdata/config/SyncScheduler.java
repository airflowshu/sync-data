package com.yunlbd.syncdata.config;

import com.yunlbd.syncdata.service.SyncService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SyncScheduler {

    @Resource
    private SyncService syncService;

    @Scheduled(cron = "#{syncProperties.schedule.cron}") // SpEL expression to access cron dynamically
    public void scheduleSync() {
        syncService.syncData();
    }

    @EventListener(ApplicationReadyEvent.class)
    @Profile("dev") // 只在dev环境执行
    public void onApplicationReady() {
        // 项目启动后立即执行同步
        syncService.syncData();
    }
}
