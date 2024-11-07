package com.yunlbd.syncdata;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAdminServer
@SpringBootApplication
@EnableConfigurationProperties
@EnableScheduling
public class SyncDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(SyncDataApplication.class, args);
    }

}
