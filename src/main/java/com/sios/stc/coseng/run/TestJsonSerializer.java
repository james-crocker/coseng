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
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.sios.stc.coseng.run.SeleniumBrowser.Browser;
import com.sios.stc.coseng.run.SeleniumWebDriver.Location;

public final class TestJsonSerializer {

    public static Map<Class<?>, JsonSerializer<?>> get() {
        Map<Class<?>, JsonSerializer<?>> serializers = new HashMap<Class<?>, JsonSerializer<?>>();

        JsonSerializer<Platform> platformVersionTypeSerializer = new JsonSerializer<Platform>() {
            @Override
            public JsonElement serialize(Platform arg0, Type arg1, JsonSerializationContext arg2) {
                if (arg0 != null) {
                    JsonParser parser = new JsonParser();
                    return parser.parse(arg0.toString().toUpperCase());
                }
                return null;
            }
        };
        serializers.put(Platform.class, platformVersionTypeSerializer);

        JsonSerializer<Browser> browserTypeSerializer = new JsonSerializer<Browser>() {
            @Override
            public JsonElement serialize(Browser arg0, Type arg1, JsonSerializationContext arg2) {
                if (arg0 != null) {
                    JsonParser parser = new JsonParser();
                    return parser.parse(arg0.toString().toUpperCase());
                }
                return null;
            }
        };
        serializers.put(Browser.class, browserTypeSerializer);

        JsonSerializer<Location> locationTypeSerializer = new JsonSerializer<Location>() {
            @Override
            public JsonElement serialize(Location arg0, Type arg1, JsonSerializationContext arg2) {
                if (arg0 != null) {
                    JsonParser parser = new JsonParser();
                    return parser.parse(arg0.toString().toUpperCase());
                }
                return null;
            }
        };
        serializers.put(Location.class, locationTypeSerializer);

        JsonSerializer<Level> levelTypeSerializer = new JsonSerializer<Level>() {
            @Override
            public JsonElement serialize(Level arg0, Type arg1, JsonSerializationContext arg2) {
                if (arg0 != null) {
                    JsonParser parser = new JsonParser();
                    return parser.parse(arg0.toString().toUpperCase());
                }
                return null;
            }
        };
        serializers.put(Level.class, levelTypeSerializer);

        JsonSerializer<BrowserVersion> browserVersionTypeSerializer = new JsonSerializer<BrowserVersion>() {
            @Override
            public JsonElement serialize(BrowserVersion arg0, Type arg1, JsonSerializationContext arg2) {
                if (arg0 != null) {
                    JsonParser parser = new JsonParser();
                    return parser.parse(arg0.toString().toUpperCase());
                }
                return null;
            }
        };
        serializers.put(BrowserVersion.class, browserVersionTypeSerializer);

        JsonSerializer<File> fileTypeSerializer = new JsonSerializer<File>() {
            @Override
            public JsonElement serialize(File arg0, Type arg1, JsonSerializationContext arg2) {
                if (arg0 != null) {
                    JsonParser parser = new JsonParser();
                    return parser.parse(arg0.getAbsolutePath());
                }
                return null;
            }
        };
        serializers.put(File.class, fileTypeSerializer);

        return serializers;
    }

}
