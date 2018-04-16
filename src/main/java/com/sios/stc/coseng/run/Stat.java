package com.sios.stc.coseng.run;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class Stat {

    boolean isValidUri      = false;
    boolean isSkipped       = false;
    boolean isConnected     = false;
    boolean isAccessible    = false;
    Integer responseCode    = null;
    String  responseMessage = null;

    /* tag; routes */
    Map<String, LinkedHashSet<URI>> tagRoutes = new LinkedHashMap<String, LinkedHashSet<URI>>();

    Set<String> getTags() {
        return tagRoutes.keySet();
    }

    LinkedHashSet<URI> getRoutes(String tag) {
        return tagRoutes.get(tag);
    }

    void put(String tag, LinkedHashSet<URI> routes) {
        tagRoutes.put(tag, routes);
    }

}
