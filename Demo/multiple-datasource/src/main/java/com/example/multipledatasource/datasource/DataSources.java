package com.example.multipledatasource.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSources {

    /**
     * @Primary这个注解是使用默认配置的意思，不用特定去指定Bean名字
     *
     */
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.second-datasource")
    public DataSource DataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name="DataSource3")
    @ConfigurationProperties(prefix = "spring.third-datasource")
    public DataSource secondDataSource(){
        return DataSourceBuilder.create().build();
    }

}
