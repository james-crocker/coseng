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

public final class CosengUri {

    @Expose
    private Boolean           enableFind = null;
    @Expose
    private CosengUriValidate validate   = null;

    private UriFound uriFound = null;

    public boolean isEnableFind() {
        return enableFind;
    }

    public CosengUriValidate getValidate() {
        return validate;
    }

    public UriFound getUriFound() {
        return uriFound;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, "uriFound");
    }

    void validateAndPrepare(Directory directory, TestNgContext context) {
        if (enableFind == null)
            enableFind = false;
        if (validate == null)
            validate = new CosengUriValidate();
        validate.validateAndPrepare();
        uriFound = new UriFound(directory, context, validate);
    }

}
