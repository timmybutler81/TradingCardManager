/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * July 13, 2025
 * DatabaseConnectionController.java
 * This class exposes and endpoint for switching the applications database connection dynamically at runtime
 * from h2 to MySql.
 */
package com.butlert.tradingcardmanager.controller;

import com.butlert.tradingcardmanager.config.DynamicDataSource;
import com.butlert.tradingcardmanager.model.DatabaseCredentialsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DatabaseConnectionController {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionController.class);
    private final DynamicDataSource routingDs;
    private final LocalContainerEntityManagerFactoryBean emfBean;
    private final ConfigurableApplicationContext configurableApplicationContext;

    @Autowired
    public DatabaseConnectionController(DataSource dynamicDataSource,
                                        LocalContainerEntityManagerFactoryBean emfBean,
                                        ConfigurableApplicationContext configurableApplicationContext) {
        this.routingDs = (DynamicDataSource) dynamicDataSource;
        this.emfBean = emfBean;
        this.configurableApplicationContext = configurableApplicationContext;
    }

    /**
     * method: switchToMySql
     * parameters: DatabaseCredentialsDTO
     * return: Response of pass or fail with message
     * purpose: Swaps the connection from h2 to mysql and tests the connection credentials
     */
    @PostMapping("/configure-database")
    public ResponseEntity<?> switchToMySql(@RequestBody DatabaseCredentialsDTO creds) {
        try {
            String url = "jdbc:mysql://" + creds.getHost() + ":" + creds.getPort() + "/" + creds.getDatabaseName();
            logger.debug("Constructed jdbc url: {}", url);

            DriverManagerDataSource mysqlDs = new DriverManagerDataSource();
            mysqlDs.setUrl(url);
            mysqlDs.setUsername(creds.getUsername());
            mysqlDs.setPassword(creds.getPassword());
            mysqlDs.setDriverClassName("com.mysql.cj.jdbc.Driver");

            try (Connection ignored = mysqlDs.getConnection()) {
                Map<Object, Object> targets = new HashMap<>(routingDs.getResolvedDataSources());
                targets.put("mysql", mysqlDs);
                routingDs.setTargetDataSources(targets);
                routingDs.setDefaultTargetDataSource(mysqlDs);
                routingDs.afterPropertiesSet();

                DynamicDataSource.setCurrentKey("mysql");
                emfBean.setDataSource(routingDs);
                emfBean.afterPropertiesSet();
                logger.info("Successfully connected to database {}", creds.getDatabaseName());

                return ResponseEntity.ok("Connected to MySQL.");
            }
        } catch (Exception e) {
            logger.error("Database connection failed", e);
            return ResponseEntity.badRequest().body("Connection failed: " + e.getMessage());
        }
    }
}
