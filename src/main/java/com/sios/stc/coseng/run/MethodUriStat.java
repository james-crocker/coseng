/*
 * Concurrent Selenium TestNG (COSENG)
 * Copyright (c) 2013-2018 SIOS Technology Corp.  All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sios.stc.coseng.run;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.testng.IInvokedMethod;

public final class MethodUriStat {

    TestNgContext context = null;

    MethodUriStat(TestNgContext context) {
        this.context = context;
    }

    /* method, found uri, stat */
    Map<IInvokedMethod, UriStat> methodUriStats = Collections
            .synchronizedMap(new LinkedHashMap<IInvokedMethod, UriStat>());

    boolean hasMethodStat() {
        IInvokedMethod method = context.getIInvokedMethod();
        if (methodUriStats.containsKey(method) && methodUriStats.get(method) != null) {
            return true;
        }
        return false;
    }

    UriStat getMethodStat() {
        IInvokedMethod method = context.getIInvokedMethod();
        return methodUriStats.get(method);
    }

    Stat getStat(URI uri) {
        return getStat((uri != null ? uri.toString() : null));
    }

    Stat getStat(String uri) {
        return getMethodStat().get(uri);
    }

    void put(URI uri, String tag, URI route) {
        put((uri != null ? uri.toString() : null), tag, route);
    }

    void put(String uri, String tag, URI route) {
        UriStat uriStat = getMethodStat();
        if (uriStat == null) {
            LinkedHashSet<URI> routes = new LinkedHashSet<URI>();
            routes.add(route);

            Stat stat = new Stat();
            stat.put(tag, routes);

            uriStat = new UriStat();
            uriStat.put(uri, stat);

            IInvokedMethod method = context.getIInvokedMethod();
            methodUriStats.put(method, uriStat);
        } else if (uriStat.get(uri) == null) {
            LinkedHashSet<URI> routes = new LinkedHashSet<URI>();
            routes.add(route);

            Stat stat = new Stat();
            stat.put(tag, routes);

            uriStat.put(uri, stat);
        } else if (uriStat.get(uri).getRoutes(tag) == null) {
            LinkedHashSet<URI> routes = new LinkedHashSet<URI>();
            routes.add(route);

            uriStat.get(uri).put(tag, routes);
        } else {
            uriStat.get(uri).getRoutes(tag).add(route);
        }
    }

    boolean isEmtpy() {
        return methodUriStats.isEmpty();
    }

    List<UriStat> getStats() {
        List<UriStat> newList = new ArrayList<UriStat>();
        for (IInvokedMethod method : methodUriStats.keySet()) {
            newList.add(methodUriStats.get(method));
        }
        return newList;
    }

    void clear() {
        methodUriStats.clear();
    }

}
