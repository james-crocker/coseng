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

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.google.gson.annotations.Expose;
import com.sios.stc.coseng.exceptions.CosengConfigException;
import com.sios.stc.coseng.gson.Deserializer;
import com.sios.stc.coseng.gson.Serializer;
import com.sios.stc.coseng.gson.Serializers;

public final class GsonSerializer {

    @Expose
    private String deserializerClassName = null;
    @Expose
    private String serializerClassName   = null;

    public GsonSerializer() {
        // do nothing (gson)
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this);
    }

    Serializers validateAndPrepare() {
        try {
            if (deserializerClassName == null || deserializerClassName.isEmpty() || serializerClassName == null
                    || serializerClassName.isEmpty())
                throw new IllegalArgumentException(
                        "Field deserializerClassName and serializerClassName must be provided");
            Class<?> deserializerClass = Class.forName(deserializerClassName);
            Class<?> serializerClass = Class.forName(serializerClassName);
            Serializers serializers = new Serializers();
            serializers.setGsonDeserializer((Deserializer) deserializerClass.newInstance());
            serializers.setGsonSerializer((Serializer) serializerClass.newInstance());
            return serializers;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new CosengConfigException(e);
        }
    }

}
