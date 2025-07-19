package com.butlert.tradingcardmanager.model;

import org.springframework.stereotype.Component;

/**
 * Data Transfer Object (DTO) for encapsulating database connection credentials.
 * <p>
 * Used to receive configuration details from the client when switching
 * the application's active data source at runtime.
 * </p>
 *
 * <p><b>Author:</b> Timothy Butler<br>
 * <b>Course:</b> CEN 3024 - Software Development 1<br>
 * <b>Date:</b> July 13, 2025</p>
 */
@Component
public class DatabaseCredentialsDTO {
    private String host;
    private String port;
    private String databaseName;
    private String username;
    private String password;

    public DatabaseCredentialsDTO() {
    }

    /**
     * Constructs a new DatabaseCredentialsDTO with all necessary connection details.
     *
     * @param host         the database host (e.g., "localhost")
     * @param port         the port number (e.g., "3306")
     * @param databaseName the name of the target database
     * @param username     the username used to authenticate
     * @param password     the password used to authenticate
     */
    public DatabaseCredentialsDTO(String host, String port, String databaseName, String username, String password) {
        this.host = host;
        this.port = port;
        this.databaseName = databaseName;
        this.username = username;
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
