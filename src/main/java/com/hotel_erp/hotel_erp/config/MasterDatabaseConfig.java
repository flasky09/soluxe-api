package com.hotel_erp.hotel_erp.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.hotel_erp.hotel_erp.modules.master",
        entityManagerFactoryRef = "masterEntityManagerFactory",
        transactionManagerRef = "masterTransactionManager"
)
public class MasterDatabaseConfig {

    @Bean(name = "masterEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean masterEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("masterDataSource") DataSource masterDataSource) {
        
        java.util.Map<String, Object> properties = new java.util.HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");

        return builder
                .dataSource(masterDataSource)
                .packages("com.hotel_erp.hotel_erp.modules.master")
                .persistenceUnit("master")
                .properties(properties)
                .build();
    }

    @Bean(name = "masterTransactionManager")
    public PlatformTransactionManager masterTransactionManager(
            @Qualifier("masterEntityManagerFactory") LocalContainerEntityManagerFactoryBean masterEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(masterEntityManagerFactory.getObject()));
    }
}
