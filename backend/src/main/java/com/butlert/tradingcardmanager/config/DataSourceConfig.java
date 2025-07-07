package com.butlert.tradingcardmanager.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * • Starts with an in-memory H2 data source so Spring Boot can boot.
 * • Wraps it in DynamicDataSource so we can swap to MySQL later.
 * • Exposes EntityManagerFactory & TxManager that look at DynamicDataSource.
 */
@Configuration
public class DataSourceConfig {

    /** 1. Default H2 datasource (boots instantly, no creds needed) */
    @Bean
    public DataSource h2DataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setUrl("jdbc:h2:mem:tradingcards;MODE=MYSQL;DB_CLOSE_DELAY=-1");
        ds.setUsername("sa");
        ds.setPassword("");
        ds.setDriverClassName("org.h2.Driver");
        return ds;
    }

    /** 2. Dynamic routing datasource – starts on H2, can add "mysql" later */
    @Bean
    @Primary
    public DynamicDataSource dynamicDataSource() {
        Map<Object, Object> targets = new HashMap<>();
        targets.put("h2", h2DataSource());           // default key = "h2"

        DynamicDataSource routing = new DynamicDataSource();
        routing.setTargetDataSources(targets);
        routing.setDefaultTargetDataSource(h2DataSource());
        DynamicDataSource.setCurrentKey("h2");       // initial lookup key
        return routing;
    }

    /** 3. EntityManagerFactory – points to DynamicDataSource */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dynamicDataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dynamicDataSource);
        emf.setPackagesToScan("com.butlert.tradingcardmanager.model");
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        // Let Hibernate auto-detect the dialect from whichever DB is active.
        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.hbm2ddl.auto", "update");
        emf.setJpaPropertyMap(props);
        return emf;
    }

    /** 4. Transaction manager wired to that factory */
    @Bean
    public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean emf) {
        return new JpaTransactionManager(emf.getObject());
    }
}