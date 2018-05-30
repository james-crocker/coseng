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

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class SeleniumBrowserOptionsJsonExclusions implements ExclusionStrategy {

    public boolean shouldSkipClass(Class<?> clazz) {
        /*-
         * org.openqa.selenium.firefox.FirefoxOptions faults on class:
         *   org.apache.commons.exec.ExecuteException, field "cause"
         */
        if (clazz.equals(org.apache.commons.exec.ExecuteException.class)) {
            return true;
        }
        return false;
    }

    public boolean shouldSkipField(FieldAttributes arg0) {
        /*-
         * org.openqa.selenium.firefox.FirefoxOptions faults on private class:
         *   org.openqa.selenium.os.OsProcess$SeleniumWatchDog; skip by field
         */
        if (arg0.getName().equals("process")) {
            return true;
        }
        return false;
    }

}
