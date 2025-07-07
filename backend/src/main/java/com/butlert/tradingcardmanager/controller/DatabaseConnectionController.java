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
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DatabaseConnectionController {

    private final DynamicDataSource routingDs;
    private final LocalContainerEntityManagerFactoryBean emfBean;
    private final ConfigurableApplicationContext configurableApplicationContext;
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionController.class);

    @Autowired
    public DatabaseConnectionController(DataSource dynamicDataSource,
                                        LocalContainerEntityManagerFactoryBean emfBean,
                                        ConfigurableApplicationContext configurableApplicationContext) {
        this.routingDs = (DynamicDataSource) dynamicDataSource;
        this.emfBean   = emfBean;
        this.configurableApplicationContext = configurableApplicationContext;
    }

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

            /* 1️⃣  Test connection */
            try (Connection ignored = mysqlDs.getConnection()) {
                /* 2️⃣  Add new target */
                Map<Object, Object> targets = new HashMap<>(routingDs.getResolvedDataSources());
                targets.put("mysql", mysqlDs);
                routingDs.setTargetDataSources(targets);
                routingDs.setDefaultTargetDataSource(mysqlDs);
                routingDs.afterPropertiesSet();        // refresh internal map

                /* 3️⃣  Point lookup key to MySQL */
                DynamicDataSource.setCurrentKey("mysql");

                /* 4️⃣  Re-initialize EntityManagerFactory so Hibernate picks up the new dialect */
                emfBean.setDataSource(routingDs);      // same bean, new DS lookup
                emfBean.afterPropertiesSet();          // rebuild the factory
                logger.info("Successfully connected to database {}", creds.getDatabaseName());

                return ResponseEntity.ok("Connected to MySQL.");
            }
        } catch (Exception e) {
            logger.error("Database connection failed", e);
            return ResponseEntity.badRequest().body("Connection failed: " + e.getMessage());
        }
    }
}
