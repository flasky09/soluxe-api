package com.hotel_erp.hotel_erp.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class TenantFlywayConfig {

    /**
     * Runs Flyway migrations for hotel1 tenant DB.
     * Uses db/migration/tenant/ scripts (V1-V17 by module).
     */
    @Bean(initMethod = "migrate")
    public Flyway hotel1Flyway(@Qualifier("hotel1DataSource") DataSource hotel1DataSource) {
        return Flyway.configure()
                .dataSource(hotel1DataSource)
                .locations("classpath:db/migration/tenant")
                .table("flyway_schema_history")
                .baselineOnMigrate(true)
                .baselineVersion("0")
                .load();
    }

    /**
     * Runs Flyway migrations for hotel3 tenant DB.
     * Same scripts as hotel1 — schema is identical per tenant.
     */
    @Bean(initMethod = "migrate")
    public Flyway hotel3Flyway(@Qualifier("hotel3DataSource") DataSource hotel3DataSource) {
        return Flyway.configure()
                .dataSource(hotel3DataSource)
                .locations("classpath:db/migration/tenant")
                .table("flyway_schema_history")
                .baselineOnMigrate(true)
                .baselineVersion("0")
                .load();
    }
}
