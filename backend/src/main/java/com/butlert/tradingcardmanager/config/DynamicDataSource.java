package com.butlert.tradingcardmanager.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> threadKey = new ThreadLocal<>();
    private static volatile String globalKey = "h2";   // <-- start on H2

    public static void setCurrentKey(String key) {
        threadKey.set(key);
        globalKey = key;                               // <-- make it global
    }
    public static void clear()  { threadKey.remove(); }

    @Override
    protected Object determineCurrentLookupKey() {
        String key = threadKey.get();
        return (key != null) ? key : globalKey;        // <-- fallback to global
    }
}

