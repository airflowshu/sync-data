package com.yunlbd.syncdata.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author yunlbd_wts
 */
@Configuration
public class MyBatisPlusConfig {

    @Bean(name = "dataFromDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.datafrom")
    public DataSource dataFromDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dataTargetDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.datatarget")
    public DataSource dataTargetDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dataFromJdbcTemplate")
    public JdbcTemplate dataFromJdbcTemplate(@Qualifier("dataFromDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "dataTargetJdbcTemplate")
    public JdbcTemplate dataTargetJdbcTemplate(@Qualifier("dataTargetDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
