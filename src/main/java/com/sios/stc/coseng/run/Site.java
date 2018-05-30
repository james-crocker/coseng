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

import java.net.URL;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.google.gson.annotations.Expose;
import com.sios.stc.coseng.exceptions.CosengConfigException;

public final class Site {

    @Expose
    private URL     baseUrl     = null;
    @Expose
    private Boolean angularApp  = null;
    @Expose
    private Boolean angular2App = null;

    public URL getBaseUrl() {
        return baseUrl;
    }

    public boolean isAngularApp() {
        return angularApp;
    }

    public boolean isAngular2App() {
        return angular2App;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    void validateAndPrepare() {
        try {
            if (angularApp == null)
                angularApp = false;
            if (angular2App == null)
                angular2App = false;
            if (Boolean.TRUE.equals(angularApp) && Boolean.TRUE.equals(angular2App))
                throw new IllegalArgumentException("Either field angularApp or angular2App may be true");
        } catch (Exception e) {
            throw new CosengConfigException(e);
        }
    }

}
