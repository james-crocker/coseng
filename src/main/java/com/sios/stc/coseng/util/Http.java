/*
 * Concurrent Selenium TestNG (COSENG)
 * Copyright (c) 2013-2017 SIOS Technology Corp.  All rights reserved.
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

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.LinkedHashMap;

import org.apache.commons.lang.StringUtils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * The Class Http offers conveniences to discern state of Http resrouces.
 *
 * @since 2.1
 * @version.coseng
 */
public final class Http {

    private static Integer responseCodeDefault    = 0;
    private static String  responseMessageDefault = Stringer.UNKNOWN;

    /* Use URI rather than URL to avoid blocking with equals */
    private static LinkedHashMap<URI, Integer> urisResponseCode    = new LinkedHashMap<URI, Integer>();
    private static LinkedHashMap<URI, String>  urisResponseMessage = new LinkedHashMap<URI, String>();
    private static LinkedHashMap<URI, Boolean> urisPassFail        = new LinkedHashMap<URI, Boolean>();

    public static boolean isAccessible(URL url) {
        try {
            URI uri = new URI(url.toExternalForm());
            if (urisPassFail.containsKey(uri))
                return urisPassFail.get(uri);
        } catch (Exception ignore) {
            // do nothing
        }
        return false;
    }

    public static Integer getResponseCode(URL url) {
        try {
            URI uri = new URI(url.toExternalForm());
            if (urisResponseCode.containsKey(uri))
                return urisResponseCode.get(uri);
        } catch (Exception ignore) {
            // do nothing
        }
        return responseCodeDefault;
    }

    public static String getResponseMessage(URL url) {
        try {
            URI uri = new URI(url.toExternalForm());
            if (urisResponseMessage.containsKey(uri))
                return urisResponseMessage.get(uri);
        } catch (Exception ignore) {
            // do nothing
        }
        return responseMessageDefault;
    }

    public static String connect(URL url, HttpMethod requestMethod, BrowserVersion browserVersion,
            Integer millisTimeout, Boolean useInsecureSsl, Boolean enableJavaScript, Boolean enableCss,
            Boolean enableDownloadImages) {
        Integer responseCode = responseCodeDefault;
        String responseMessage = responseMessageDefault;
        String messageBody = StringUtils.EMPTY;
        try {
            URI uri = new URI(url.toExternalForm());
            /* Only connect once for any given URI */
            if (!hasResponse(uri)) {
                if (requestMethod == null)
                    requestMethod = HttpMethod.GET;
                if (browserVersion == null)
                    browserVersion = BrowserVersion.BEST_SUPPORTED;
                if (millisTimeout == null || millisTimeout < 0)
                    millisTimeout = 3000;
                if (useInsecureSsl == null)
                    useInsecureSsl = false;
                if (enableJavaScript == null)
                    enableJavaScript = false;
                if (enableCss == null)
                    enableCss = false;
                if (enableDownloadImages == null)
                    enableDownloadImages = false;
                try {
                    WebRequest webRequest = new WebRequest(url, requestMethod);
                    WebClient webClient = new WebClient(browserVersion);
                    WebClientOptions webClientOptions = webClient.getOptions();
                    webClientOptions.setPrintContentOnFailingStatusCode(false);
                    webClientOptions.setThrowExceptionOnFailingStatusCode(true);
                    webClientOptions.setThrowExceptionOnScriptError(false);
                    webClientOptions.setUseInsecureSSL(useInsecureSsl);
                    webClientOptions.setTimeout(millisTimeout);
                    webClientOptions.setJavaScriptEnabled(enableJavaScript);
                    webClientOptions.setCssEnabled(enableCss);
                    webClientOptions.setDownloadImages(enableDownloadImages);
                    Object page = null;
                    page = webClient.getPage(webRequest);
                    webClient.close();
                    if (page instanceof UnexpectedPage) {
                        responseCode = ((UnexpectedPage) page).getWebResponse().getStatusCode();
                        responseMessage = ((UnexpectedPage) page).getWebResponse().getStatusMessage();
                        messageBody = ((UnexpectedPage) page).getWebResponse().getContentAsString();
                    } else if (page instanceof HtmlPage) {
                        responseCode = ((HtmlPage) page).getWebResponse().getStatusCode();
                        responseMessage = ((HtmlPage) page).getWebResponse().getStatusMessage();
                        messageBody = ((HtmlPage) page).getWebResponse().getContentAsString();
                    }
                    urisResponseCode.put(uri, responseCode);
                    urisResponseMessage.put(uri, responseMessage);
                    urisPassFail.put(uri, true);
                } catch (FailingHttpStatusCodeException | IOException e) {
                    if (e instanceof FailingHttpStatusCodeException)
                        responseCode = ((FailingHttpStatusCodeException) e).getStatusCode();
                    urisResponseCode.put(uri, responseCode);
                    urisResponseMessage.put(uri, e.getMessage());
                    urisPassFail.put(uri, false);
                }
            } else {
                urisResponseCode.put(uri, responseCode);
                urisResponseMessage.put(uri, responseMessage);
                urisPassFail.put(uri, false);
            }
        } catch (Exception ignoree) {
            // do nothing
        }
        return messageBody;
    }

    private static boolean hasResponse(URI uri) {
        if (uri != null && urisResponseCode.containsKey(uri) && urisResponseMessage.containsKey(uri)
                && urisPassFail.containsKey(uri))
            return true;
        return false;
    }

}
