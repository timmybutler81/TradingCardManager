package com.butlert.tradingcardmanager.controller;

import com.butlert.tradingcardmanager.config.DynamicDataSource;
import com.butlert.tradingcardmanager.model.DatabaseCredentialsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import java.sql.Connection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DatabaseConnectionControllerTest {

    @Mock
    private DynamicDataSource mockRoutingDataSource;

    @Mock
    private LocalContainerEntityManagerFactoryBean mockEmfBean;

    @Mock
    private ConfigurableApplicationContext mockAppContext;

    @InjectMocks
    private DatabaseConnectionController controller;

    @Captor
    private ArgumentCaptor<Map<Object, Object>> targetCaptor;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        controller = new DatabaseConnectionController(mockRoutingDataSource, mockEmfBean, mockAppContext);
    }

    @Test
    void testSwitchToMySql_successfulConnection_returnsOk() throws Exception {
        DatabaseCredentialsDTO creds = new DatabaseCredentialsDTO();
        creds.setHost("localhost");
        creds.setPort("3306");
        creds.setDatabaseName("test_db");
        creds.setUsername("user");
        creds.setPassword("pass");

        DriverManagerDataSource mockDataSource = mock(DriverManagerDataSource.class);
        Connection mockConnection = mock(Connection.class);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);

        when(mockRoutingDataSource.getResolvedDataSources()).thenReturn(Map.of());

        TestableDatabaseConnectionController controller = new TestableDatabaseConnectionController(
                mockRoutingDataSource, mockEmfBean, mockAppContext, mockDataSource
        );

        ResponseEntity<?> response = controller.switchToMySql(creds);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Connected to MySQL.", response.getBody());
    }

    @Test
    void testSwitchToMySql_connectionFails_returnsBadRequest() {
        DatabaseCredentialsDTO creds = new DatabaseCredentialsDTO();
        creds.setHost("localhost");
        creds.setPort("3306");
        creds.setDatabaseName("bad_db");
        creds.setUsername("invalid");
        creds.setPassword("wrong");

        ResponseEntity<?> response = controller.switchToMySql(creds);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Connection failed"));
    }
}