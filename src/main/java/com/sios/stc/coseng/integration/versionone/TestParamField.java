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

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.google.gson.annotations.Expose;

/**
 * The Class ParamFields.
 *
 * @since 3.0
 * @version.coseng
 */
public final class TestParamField {

    @Expose
    private final String      name   = null;
    @Expose
    private final String      value  = null;
    @Expose
    private final List<Field> fields = null;

    String getParamName() {
        return name;
    }

    String getParamValue() {
        return value;
    }

    List<Field> getFields() {
        return fields;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
