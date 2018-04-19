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
package com.sios.stc.coseng.aut;

import org.openqa.selenium.WebDriver;

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
public final class CosengTest {

    private static final InheritableThreadLocal<Test>    test    = new InheritableThreadLocal<Test>();
    private static final InheritableThreadLocal<Angular> angular = new InheritableThreadLocal<Angular>();
    private static final InheritableThreadLocal<Window>  window  = new InheritableThreadLocal<Window>();
    private static final InheritableThreadLocal<Log>     log     = new InheritableThreadLocal<Log>();
    private static final InheritableThreadLocal<Uri>     uri     = new InheritableThreadLocal<Uri>();

    public CosengTest() {
        test.set(TestNgListener.getTest());
        angular.set(new Angular(test.get()));
        window.set(new Window(test.get()));
        log.set(new Log(test.get(), window.get()));
        uri.set(new Uri(test.get(), log.get(), angular.get()));
    }

    public static Test getTest() {
        return test.get();
    }

    public static Angular getAngular() {
        return angular.get();
    }

    public static Window getWindow() {
        return window.get();
    }

    public static Log getLog() {
        return log.get();
    }

    public static Uri getUri() {
        return uri.get();
    }

    public static WebDrivers getWebDrivers() {
        return test.get().getSelenium().getWebDriverContext().getWebDrivers();
    }

    public static WebDriver getWebDriver() {
        return test.get().getSelenium().getWebDriverContext().getWebDrivers().getWebDriver();
    }

}
