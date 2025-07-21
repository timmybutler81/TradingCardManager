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

/**
 * Controller responsible for switching the application's database connection at runtime.
 * <p>
 * This allows the Trading Card Manager to dynamically connect to a MySQL database using
 * credentials provided by the GUI frontend, replacing the default in-memory H2 connection.
 * </p>
 *
 * <p><b>Author:</b> Timothy Butler<br>
 * <b>Course:</b> CEN 3024 - Software Development 1<br>
 * <b>Date:</b> July 18, 2025</p>
 */
@RestController
@RequestMapping("/api")
public class DatabaseConnectionController {

    /**
     * Logger instance for recording application events and errors in the
     * {@link com.butlert.tradingcardmanager.controller.DatabaseConnectionController}.
     * <p>
     * Uses SLF4J's {@link org.slf4j.LoggerFactory} for standardized logging.
     * Helps in debugging and tracing database connection handling.
     */
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionController.class);

    /**
     * Dynamic data source router used to switch between data sources.
     */
    final DynamicDataSource routingDs;

    /**
     * JPA EntityManager factory used to reconfigure the active data source.
     */
    final LocalContainerEntityManagerFactoryBean emfBean;

    /**
     * Spring application context, required to refresh bean configuration if needed.
     */
    private final ConfigurableApplicationContext configurableApplicationContext;

    /**
     * Constructs a new DatabaseConnectionController with the required configuration components.
     *
     * @param dynamicDataSource              the dynamic data source used for routing
     * @param emfBean                        the JPA entity manager factory bean
     * @param configurableApplicationContext the Spring application context
     */
    @Autowired
    public DatabaseConnectionController(DataSource dynamicDataSource,
                                        LocalContainerEntityManagerFactoryBean emfBean,
                                        ConfigurableApplicationContext configurableApplicationContext) {
        this.routingDs = (DynamicDataSource) dynamicDataSource;
        this.emfBean = emfBean;
        this.configurableApplicationContext = configurableApplicationContext;
    }

    /**
     * Switches the application's active database connection to MySQL using the provided credentials.
     * Also verifies the connection before switching and resets the EntityManagerFactory to apply changes.
     *
     * @param creds the database credentials (host, port, database name, username, and password)
     * @return a ResponseEntity indicating success or failure of the database switch
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

            // Test connection
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
