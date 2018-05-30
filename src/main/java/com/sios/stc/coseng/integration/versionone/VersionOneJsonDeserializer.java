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
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sios.stc.coseng.Triggers.TestPhase;
import com.sios.stc.coseng.Triggers.TriggerOn;
import com.sios.stc.coseng.gson.Deserializer;

public final class VersionOneJsonDeserializer extends Deserializer {

    @Override
    public Map<Class<?>, JsonDeserializer<?>> get() {
        Map<Class<?>, JsonDeserializer<?>> deserializers = new LinkedHashMap<Class<?>, JsonDeserializer<?>>();

        JsonDeserializer<TriggerOn> triggerOnTypeDeserializer = new JsonDeserializer<TriggerOn>() {
            public TriggerOn deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException {
                return TriggerOn.valueOf(json.getAsString().toUpperCase());
            }
        };
        deserializers.put(TriggerOn.class, triggerOnTypeDeserializer);

        JsonDeserializer<TestPhase> phaseTypeDeserializer = new JsonDeserializer<TestPhase>() {
            public TestPhase deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException {
                return TestPhase.valueOf(json.getAsString().toUpperCase());
            }
        };
        deserializers.put(TestPhase.class, phaseTypeDeserializer);

        return deserializers;
    }

}
