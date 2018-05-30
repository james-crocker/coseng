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
package com.sios.stc.coseng.util;

import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpResponse {

    static Integer codeDefault    = 0;
    static String  messageDefault = Stringer.UNKNOWN;

    private static Map<URI, Integer> urisResponseCode    = Collections
            .synchronizedMap(new LinkedHashMap<URI, Integer>());
    private static Map<URI, String>  urisResponseMessage = Collections
            .synchronizedMap(new LinkedHashMap<URI, String>());
    private static Map<URI, Boolean> urisPassFail        = Collections
            .synchronizedMap(new LinkedHashMap<URI, Boolean>());

    Integer getCode(URL url) {
        try {
            URI uri = new URI(url.toExternalForm());
            if (urisResponseCode.containsKey(uri))
                return urisResponseCode.get(uri);
        } catch (Exception ignore) {
            // do nothing
        }
        return codeDefault;
    }

    void putCode(URI uri, Integer code) {
        urisResponseCode.put(uri, code);
    }

    String getMessage(URL url) {
        try {
            URI uri = new URI(url.toExternalForm());
            if (urisResponseMessage.containsKey(uri))
                return urisResponseMessage.get(uri);
        } catch (Exception ignore) {
            // do nothing
        }
        return messageDefault;
    }

    void putMessage(URI uri, String message) {
        urisResponseMessage.put(uri, message);
    }

    boolean isPassed(URL url) {
        try {
            URI uri = new URI(url.toExternalForm());
            if (urisPassFail.containsKey(uri))
                return urisPassFail.get(uri);
        } catch (Exception ignore) {
            // do nothing
        }
        return false;
    }

    void setPassed(URI uri, boolean passed) {
        urisPassFail.put(uri, passed);
    }

    boolean hasResponse(URI uri) {
        if (uri != null && urisResponseCode.containsKey(uri) && urisResponseMessage.containsKey(uri)
                && urisPassFail.containsKey(uri))
            return true;
        return false;
    }

}
