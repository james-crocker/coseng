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
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.google.gson.annotations.Expose;
import com.sios.stc.coseng.customoptions.CustomOptions;
import com.sios.stc.coseng.exceptions.CosengConfigException;
import com.sios.stc.coseng.gson.Serializers;
import com.sios.stc.coseng.util.Resource;
import com.sios.stc.coseng.util.UriUtil;

public final class CosengCustomOptions {

    @Expose
    private Set<URI>       classPaths       = null;
    @Expose
    private String         optionsClassName = null;
    @Expose
    private GsonSerializer gsonSerializer   = null;
    @Expose
    private URI            optionsJson      = null;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    CustomOptions validateAndPrepare(Test test) {
        try {
            if (optionsClassName == null || optionsClassName.isEmpty() || optionsJson == null)
                throw new IllegalArgumentException("Field optionsClassName and optionsJson must be provided");
            if (classPaths != null) {
                if (classPaths.contains(null))
                    throw new IllegalArgumentException("Field classPaths may not contain null elements");
                if (!classPaths.isEmpty()) {
                    Set<URI> canonicalClassPaths = new HashSet<URI>();
                    for (URI uri : classPaths) {
                        canonicalClassPaths.add(UriUtil.getCanonical(uri));
                    }
                    classPaths = canonicalClassPaths;
                    Resource.addClassPathsToThread(classPaths);
                }
            }
            /* Get the concrete class */
            Class<?> optionsClass = Class.forName(optionsClassName);
            CustomOptions options = (CustomOptions) optionsClass.newInstance();
            optionsJson = UriUtil.getCanonical(optionsJson);
            Serializers serializers = null;
            if (gsonSerializer != null)
                serializers = gsonSerializer.validateAndPrepare();
            options.validateAndPrepare(test, serializers, optionsJson);
            return options;
        } catch (Exception e) {
            throw new CosengConfigException(e);
        }
    }

}
