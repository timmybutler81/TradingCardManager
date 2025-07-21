package com.butlert.tradingcardmanager.controller;

import com.butlert.tradingcardmanager.config.DynamicDataSource;
import com.butlert.tradingcardmanager.model.DatabaseCredentialsDTO;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class TestableDatabaseConnectionController extends DatabaseConnectionController {

    private final DriverManagerDataSource testDataSource;

    public TestableDatabaseConnectionController(
            DataSource dynamicDataSource,
            LocalContainerEntityManagerFactoryBean emfBean,
            ConfigurableApplicationContext context,
            DriverManagerDataSource testDataSource
    ) {
        super(dynamicDataSource, emfBean, context);
        this.testDataSource = testDataSource;
    }

    @Override
    public ResponseEntity<?> switchToMySql(DatabaseCredentialsDTO creds) {
        try {
            Connection conn = testDataSource.getConnection();
            Map<Object, Object> targets = new HashMap<>(getRoutingDataSource().getResolvedDataSources());
            targets.put("mysql", testDataSource);
            getRoutingDataSource().setTargetDataSources(targets);
            getRoutingDataSource().setDefaultTargetDataSource(testDataSource);
            getRoutingDataSource().afterPropertiesSet();

            DynamicDataSource.setCurrentKey("mysql");
            getEmfBean().setDataSource(getRoutingDataSource());
            getEmfBean().afterPropertiesSet();

            return ResponseEntity.ok("Connected to MySQL.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Connection failed: " + e.getMessage());
        }
    }

    private DynamicDataSource getRoutingDataSource() {
        return (DynamicDataSource) super.routingDs;
    }

    private LocalContainerEntityManagerFactoryBean getEmfBean() {
        return super.emfBean;
    }
}
