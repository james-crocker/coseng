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
package com.sios.stc.coseng.aut;

import com.paulhammant.ngwebdriver.NgWebDriver;
import com.sios.stc.coseng.run.Test;

public final class Angular {

    private Test test = null;

    Angular(Test test) {
        this.test = test;
    }

    public void waitToFinish() {
        /* ngWebDriver won't be available until TestNG thread starts a web driver */
        NgWebDriver ngWebDriver = test.getSelenium().getWebDriverContext().getWebDrivers().getNgWebDriver();
        /* 1.0 NgWebDriver had waitForAngular2RequestsToFinish() */
        ngWebDriver.waitForAngularRequestsToFinish();
    }

}
