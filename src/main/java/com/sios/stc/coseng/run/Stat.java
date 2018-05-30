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
