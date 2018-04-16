package com.sios.stc.coseng.run;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public final class UriStat {

    /* found url; stat */
    Map<String, Stat> uriStats = new LinkedHashMap<String, Stat>();

    Set<String> keySet() {
        return uriStats.keySet();
    }

    Stat get(String uri) {
        return uriStats.get(uri);
    }

    void put(String uri, Stat stat) {
        uriStats.put(uri, stat);
    }

}
