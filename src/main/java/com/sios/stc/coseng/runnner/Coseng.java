/*
 * Concurrent Selenium TestNG (COSENG)
 * Copyright (c) 2013-2017 SIOS Technology Corp.  All rights reserved.
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
package com.sios.stc.coseng.runnner;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.internal.WrapsDriver;

import com.sios.stc.coseng.run.Test;
import com.sios.stc.coseng.run.TestNgListener;
import com.sios.stc.coseng.run.WebDrivers;

/**
 * The Class CosengRunner. This is the class that each TestNG class under test
 * must extend to access the appropriate web driver at the requested depth of
 * parallelism. This class provides convenience methods to access the available
 * web driver and its derived objects such as Actions and JavascriptExecutor as
 * well as manage Selenium WebElements as objects for the level of parallelism.
 * <b>Note:</b> Call these methods from within test class methods.
 * <b>Caution:</b> Unless you manage the test context, calling these methods
 * outside of the TestNG test class methods will have unintended consequences.
 *
 * @since 2.0
 * @version.coseng
 */
public class Coseng implements WrapsDriver {

    protected final Test    cosengTest    = TestNgListener.getTest();
    protected final Angular cosengAngular = new Angular(cosengTest);
    protected final Window  cosengWindow  = new Window(cosengTest);
    protected final Log     cosengLog     = new Log(cosengTest, cosengWindow);
    protected final Uri     cosengUri     = new Uri(cosengTest, cosengLog, cosengAngular);

    // public static WebDrivers webDrivers() {
    // return
    // TestNgListener.getTest().getSelenium().getWebDriverContext().getWebDrivers();
    // }

    public WebDrivers getWebDrivers() {
        return cosengTest.getSelenium().getWebDriverContext().getWebDrivers();
    }

    @Override
    public WebDriver getWrappedDriver() {
        return cosengTest.getSelenium().getWebDriverContext().getWebDrivers().getWebDriver();
    }

    // public static WebDriver getWebDriver() {
    // return
    // TestNgListener.getTest().getSelenium().getWebDriverContext().getWebDrivers().getWebDriver();
    // }

}
