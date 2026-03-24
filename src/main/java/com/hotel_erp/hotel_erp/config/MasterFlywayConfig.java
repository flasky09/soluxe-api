package com.hotel_erp.hotel_erp.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class MasterFlywayConfig {

    /**
     * Runs Flyway migrations for the Master DB (tenant_config table).
     * Uses db/migration/master/ scripts.
     * baselineOnMigrate=true is safe for existing Railway DBs — marks V1 as baseline
     * without re-running it if the flyway_schema_history table doesn't yet exist.
     */
    @Bean(initMethod = "migrate")
    public Flyway masterFlyway(@Qualifier("masterDataSource") DataSource masterDataSource) {
        return Flyway.configure()
                .dataSource(masterDataSource)
                .locations("classpath:db/migration/master")
                .table("flyway_schema_history_master")
                .baselineOnMigrate(true)
                .baselineVersion("0")
                .load();
    }
}
