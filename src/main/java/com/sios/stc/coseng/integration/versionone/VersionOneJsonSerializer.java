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
package com.sios.stc.coseng.integration.versionone;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.sios.stc.coseng.Triggers.TestPhase;
import com.sios.stc.coseng.Triggers.TriggerOn;
import com.sios.stc.coseng.gson.Serializer;

public final class VersionOneJsonSerializer extends Serializer {

    @Override
    public Map<Class<?>, JsonSerializer<?>> get() {
        Map<Class<?>, JsonSerializer<?>> serializers = new HashMap<Class<?>, JsonSerializer<?>>();

        JsonSerializer<TriggerOn> triggerOnTypeSerializer = new JsonSerializer<TriggerOn>() {
            @Override
            public JsonElement serialize(TriggerOn arg0, Type arg1, JsonSerializationContext arg2) {
                if (arg0 != null) {
                    JsonParser parser = new JsonParser();
                    return parser.parse(arg0.toString().toUpperCase());
                }
                return null;
            }
        };
        serializers.put(TriggerOn.class, triggerOnTypeSerializer);

        JsonSerializer<TestPhase> phaseTypeSerializer = new JsonSerializer<TestPhase>() {
            @Override
            public JsonElement serialize(TestPhase arg0, Type arg1, JsonSerializationContext arg2) {
                if (arg0 != null) {
                    JsonParser parser = new JsonParser();
                    return parser.parse(arg0.toString().toUpperCase());
                }
                return null;
            }
        };
        serializers.put(TestPhase.class, phaseTypeSerializer);

        return serializers;
    }

}
