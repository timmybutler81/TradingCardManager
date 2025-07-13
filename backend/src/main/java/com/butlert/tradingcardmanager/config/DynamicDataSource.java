/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * July 13, 2025
 * DynamicDataSource.java
 * This class handles the configuration for the data source. It handles run time initialization and switching
 * over to a mysql database.
 */
package com.butlert.tradingcardmanager.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> threadKey = new ThreadLocal<>();
    private static volatile String globalKey = "h2";

    /**
     * method: setCurrentKey
     * parameters: key
     * return: void
     * purpose: Sets the current context-specific look up key for selecting data source
     */
    public static void setCurrentKey(String key) {
        threadKey.set(key);
        globalKey = key;
    }

    /**
     * method: clear
     * parameters: none
     * return: void
     * purpose: Clears the thread local key for the current thread
     */
    public static void clear() {
        threadKey.remove();
    }

    /**
     * method: determineCurrentLookupKey
     * parameters: none
     * return: Object
     * purpose: Determines which data source key to use for the current operation
     */
    @Override
    protected Object determineCurrentLookupKey() {
        String key = threadKey.get();
        return (key != null) ? key : globalKey;
    }
}

