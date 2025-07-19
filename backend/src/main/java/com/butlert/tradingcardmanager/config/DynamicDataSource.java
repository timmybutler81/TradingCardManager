package com.butlert.tradingcardmanager.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Dynamic data source that allows runtime switching between multiple databases.
 * <p>
 * Extends {@link org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource}
 * and uses a thread-local key to route database calls to either H2 or MySQL based on the current context.
 * </p>
 *
 * <p><b>Author:</b> Timothy Butler<br>
 * <b>Course:</b> CEN 3024 - Software Development 1<br>
 * <b>Date:</b> July 13, 2025</p>
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> threadKey = new ThreadLocal<>();
    private static volatile String globalKey = "h2";

    /**
     * Default constructor for DynamicDataSource.
     * Initializes the routing logic using the built-in AbstractRoutingDataSource behavior.
     */
    public DynamicDataSource() {
        super();
    }

    /**
     * Sets the current context-specific lookup key for selecting the data source.
     *
     * @param key the data source key (e.g., "h2" or "mysql")
     */
    public static void setCurrentKey(String key) {
        threadKey.set(key);
        globalKey = key;
    }

    /**
     * Clears the thread-local key for the current thread.
     */
    public static void clear() {
        threadKey.remove();
    }

    /**
     * Determines which data source key to use for the current operation.
     *
     * @return the current data source key, falling back to the global key if none is set
     */
    @Override
    protected Object determineCurrentLookupKey() {
        String key = threadKey.get();
        return (key != null) ? key : globalKey;
    }
}

