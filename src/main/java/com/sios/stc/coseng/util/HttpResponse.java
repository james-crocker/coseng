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
