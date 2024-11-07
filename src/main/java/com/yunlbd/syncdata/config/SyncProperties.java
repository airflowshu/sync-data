package com.yunlbd.syncdata.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "sync")
public class SyncProperties {
    private List<String> tables;
    private Schedule schedule;

    @Data
    public static class Schedule {
        public String cron;
        public boolean sendmail = false;
        public String mailfrom;
    }
}