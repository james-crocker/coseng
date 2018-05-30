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
import com.sios.stc.coseng.customoptions.CustomOptions;

public final class Coseng {

    @Expose
    private CosengCustomOptions customOptions = null;
    @Expose
    private CosengScreenshot    screenshot    = null;
    @Expose
    private CosengUri           uri           = null;

    private CustomOptions options = null;

    public CustomOptions getCustomOptions() {
        return options;
    }

    public CosengScreenshot getScreenshot() {
        return screenshot;
    }

    public CosengUri getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, "options");
    }

    void validateAndPrepare(Test test) {
        if (customOptions != null)
            options = customOptions.validateAndPrepare(test);
        if (screenshot == null)
            screenshot = new CosengScreenshot();
        screenshot.validateAndPrepare();
        if (uri == null)
            uri = new CosengUri();
        uri.validateAndPrepare(test.getTestNg().getDirectory(), test.getTestNg().getContext());
    }

}
