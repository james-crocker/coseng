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

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.openqa.selenium.Platform;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sios.stc.coseng.run.SeleniumBrowser.Browser;
import com.sios.stc.coseng.run.SeleniumWebDriver.Location;
import com.sios.stc.coseng.util.Stringer;

public final class TestJsonDeserializer {

    public static Map<Class<?>, JsonDeserializer<?>> get() {
        Map<Class<?>, JsonDeserializer<?>> deserializers = new HashMap<Class<?>, JsonDeserializer<?>>();

        JsonDeserializer<Platform> platformTypeDeserializer = new JsonDeserializer<Platform>() {
            public Platform deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException {
                String platform = json.getAsString().toUpperCase();
                if (Platform.valueOf(platform) != null) {
                    return Platform.valueOf(platform);
                }
                throw new JsonParseException("Unable to get Platform from " + Stringer.wrapBracket(platform));
            }
        };
        deserializers.put(Platform.class, platformTypeDeserializer);

        JsonDeserializer<Browser> browserTypeDeserializer = new JsonDeserializer<Browser>() {
            public Browser deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException {
                String browser = json.getAsString().toUpperCase();
                if (Browser.valueOf(browser) != null) {
                    return Browser.valueOf(browser);
                }
                throw new JsonParseException("Unable to get Browser from " + Stringer.wrapBracket(browser));
            }
        };
        deserializers.put(Browser.class, browserTypeDeserializer);

        JsonDeserializer<Location> locationTypeDeserializer = new JsonDeserializer<Location>() {
            public Location deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException {
                String location = json.getAsString().toUpperCase();
                if (Location.valueOf(location) != null) {
                    return Location.valueOf(location);
                }
                throw new JsonParseException("Unable to get Location from " + Stringer.wrapBracket(location));
            }
        };
        deserializers.put(Location.class, locationTypeDeserializer);

        JsonDeserializer<Level> levelTypeDeserializer = new JsonDeserializer<Level>() {
            public Level deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException {
                String level = json.getAsString().toUpperCase();
                if (Level.ALL.toString().equalsIgnoreCase(level)) {
                    return Level.ALL;
                } else if (Level.CONFIG.toString().equalsIgnoreCase(level)) {
                    return Level.CONFIG;
                } else if (Level.FINE.toString().equalsIgnoreCase(level)) {
                    return Level.FINE;
                } else if (Level.FINER.toString().equalsIgnoreCase(level)) {
                    return Level.FINER;
                } else if (Level.FINEST.toString().equalsIgnoreCase(level)) {
                    return Level.FINEST;
                } else if (Level.INFO.toString().equalsIgnoreCase(level)) {
                    return Level.INFO;
                } else if (Level.OFF.toString().equalsIgnoreCase(level)) {
                    return Level.OFF;
                } else if (Level.SEVERE.toString().equalsIgnoreCase(level)) {
                    return Level.SEVERE;
                } else if (Level.WARNING.toString().equalsIgnoreCase(level)) {
                    return Level.WARNING;
                }
                throw new JsonParseException("Unable to get Level from " + Stringer.wrapBracket(level));
            }
        };
        deserializers.put(Level.class, levelTypeDeserializer);

        JsonDeserializer<BrowserVersion> browserVersionTypeDeserializer = new JsonDeserializer<BrowserVersion>() {
            public BrowserVersion deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException {
                String browserVersion = json.getAsString().toUpperCase();
                /*
                 * BrowserVersion has no valueOf; BEST_SUPPORTED just references named browser
                 * versions. eg Chrome. Additionally the names are mixed case.
                 */
                if (BrowserVersion.BEST_SUPPORTED.toString().equalsIgnoreCase(browserVersion)) {
                    return BrowserVersion.BEST_SUPPORTED;
                } else if (BrowserVersion.CHROME.toString().equalsIgnoreCase(browserVersion)) {
                    return BrowserVersion.CHROME;
                } else if (BrowserVersion.EDGE.toString().equalsIgnoreCase(browserVersion)) {
                    return BrowserVersion.EDGE;
                } else if (BrowserVersion.FIREFOX_45.toString().equalsIgnoreCase(browserVersion)) {
                    return BrowserVersion.FIREFOX_45;
                } else if (BrowserVersion.FIREFOX_52.toString().equalsIgnoreCase(browserVersion)) {
                    return BrowserVersion.FIREFOX_52;
                } else if (BrowserVersion.INTERNET_EXPLORER.toString().equalsIgnoreCase(browserVersion)) {
                    return BrowserVersion.INTERNET_EXPLORER;
                }
                throw new JsonParseException(
                        "Unable to get BrowserVersion from " + Stringer.wrapBracket(browserVersion));
            }
        };
        deserializers.put(BrowserVersion.class, browserVersionTypeDeserializer);

        JsonDeserializer<File> fileTypeDeserializer = new JsonDeserializer<File>() {
            public File deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException {
                String file = json.getAsString();
                if (file != null) {
                    return new File(file);
                }
                throw new JsonParseException("Unable to get File from " + Stringer.wrapBracket(file));
            }
        };
        deserializers.put(File.class, fileTypeDeserializer);

        return deserializers;
    }

}
