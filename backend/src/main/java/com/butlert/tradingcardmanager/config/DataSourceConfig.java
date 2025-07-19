package com.butlert.tradingcardmanager.config;

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
 * Configuration class for the application's data source setup.
 * <p>
 * This class handles runtime initialization of the database and supports switching
 * from the default in-memory H2 database to a MySQL database at runtime.
 * It wraps the data sources in a {@link DynamicDataSource}, which allows dynamic routing.
 * </p>
 * <p>
 * It also defines the JPA {@link org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean}
 * and {@link org.springframework.transaction.PlatformTransactionManager} beans
 * so that Spring Boot can manage persistence through the dynamic data source.
 * </p>
 *
 * <p><b>Author:</b> Timothy Butler<br>
 * <b>Course:</b> CEN 3024 - Software Development 1<br>
 * <b>Date:</b> July 13, 2025</p>
 */
@Configuration
public class DataSourceConfig {

    /**
     * Default constructor for {@code DataSourceConfig}.
     * <p>
     * This constructor is required for Spring to instantiate the configuration class.
     * All initialization and bean definitions are handled through annotated {@code @Bean} methods.
     * </p>
     */
    public DataSourceConfig() {
    }

    /**
     * Creates the default in-memory H2 datasource used when the application starts.
     * This allows the application to boot instantly without requiring external credentials.
     *
     * @return the configured H2 {@link DataSource}
     */
    @Bean
    public DataSource h2DataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setUrl("jdbc:h2:mem:tradingcards;MODE=MYSQL;DB_CLOSE_DELAY=-1");
        ds.setUsername("sa");
        ds.setPassword("");
        ds.setDriverClassName("org.h2.Driver");
        return ds;
    }

    /**
     * Configures the dynamic routing data source.
     * Starts with the H2 data source and supports adding other sources (e.g., MySQL) at runtime.
     *
     * @return the configured {@link DynamicDataSource}
     */
    @Bean
    @Primary
    public DynamicDataSource dynamicDataSource() {
        Map<Object, Object> targets = new HashMap<>();
        targets.put("h2", h2DataSource());

        DynamicDataSource routing = new DynamicDataSource();
        routing.setTargetDataSources(targets);
        routing.setDefaultTargetDataSource(h2DataSource());
        DynamicDataSource.setCurrentKey("h2");
        return routing;
    }

    /**
     * Configures the JPA EntityManagerFactory to use the dynamic data source.
     * Scans the model package and applies basic Hibernate properties.
     *
     * @param dynamicDataSource the dynamic data source to be used
     * @return the configured {@link LocalContainerEntityManagerFactoryBean}
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dynamicDataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dynamicDataSource);
        emf.setPackagesToScan("com.butlert.tradingcardmanager.model");
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.hbm2ddl.auto", "update");
        emf.setJpaPropertyMap(props);
        return emf;
    }

    /**
     * Configures the transaction manager to use the provided EntityManagerFactory.
     *
     * @param emf the entity manager factory
     * @return the configured {@link PlatformTransactionManager}
     */
    @Bean
    public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean emf) {
        return new JpaTransactionManager(emf.getObject());
    }
}