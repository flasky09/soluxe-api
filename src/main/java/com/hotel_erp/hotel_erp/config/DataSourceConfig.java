package com.hotel_erp.hotel_erp.config;

import com.hotel_erp.hotel_erp.config.tenant.TenantRoutingDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    // 1. MASTER DATASOURCE
    @Bean
    @ConfigurationProperties("spring.datasource.master")
    public DataSourceProperties masterDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource masterDataSource() {
        return masterDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }

    // 2. TENANT DATASOURCES
    @Bean
    @ConfigurationProperties("app.datasource.tenant.hotel1")
    public DataSourceProperties hotel1DataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource hotel1DataSource() {
        return hotel1DataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }

    // TENANT - Hotel 3
    @Bean
    @ConfigurationProperties("app.datasource.tenant.hotel3")
    public DataSourceProperties hotel3DataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource hotel3DataSource() {
        return hotel3DataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }

    // 3. ROUTING DATASOURCE (For all @Tenant operations)
    @Primary
    @Bean
    public DataSource tenantRoutingDataSource(
            @Qualifier("hotel1DataSource") DataSource hotel1DataSource,
            @Qualifier("hotel3DataSource") DataSource hotel3DataSource) {

        TenantRoutingDataSource routingDataSource = new TenantRoutingDataSource();

        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("hotel1", hotel1DataSource);
        dataSourceMap.put("hotel3", hotel3DataSource);

        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(hotel1DataSource);

        return routingDataSource;
    }
}
