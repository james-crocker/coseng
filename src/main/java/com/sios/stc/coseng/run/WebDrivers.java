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

import java.util.Map;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.seleniumhq.selenium.fluent.FluentWebDriver;

import com.paulhammant.ngwebdriver.NgWebDriver;

public final class WebDrivers {

    /*
     * https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/
     * ConcurrentHashMap.html
     */
    private static final ThreadLocal<WebDriverCollection> webDriverCollection = new ThreadLocal<WebDriverCollection>();

    public String getBrowserName() {
        return getCollection().getBrowserName();
    }

    public String getBrowserVersion() {
        return getCollection().getBrowserVersion();
    }

    public Platform getPlatform() {
        return getCollection().getPlatform();
    }

    public Map<String, ?> getWebDriverCapabilities() {
        return getCollection().getCapabilities();
    }

    public RemoteWebDriver getRemoteWebDriver() {
        return getCollection().getRemoteWebDriver();
    }

    public WebDriver getWebDriver() {
        return getCollection().getRemoteWebDriver();
    }

    public WebDriverWait getWebDriverWait() {
        return getCollection().getWebDriverWait();
    }

    public Actions getActions() {
        return getCollection().getActions();
    }

    public JavascriptExecutor getJsExecutor() {
        return getCollection().getJsExecutor();
    }

    public NgWebDriver getNgWebDriver() {
        return getCollection().getNgWebDriver();
    }

    public FluentWebDriver getFluentWebDriver() {
        return getCollection().getFluentWebDriver();
    }

    public boolean hasWebDriverCollection() {
        if (webDriverCollection.get() != null)
            return true;
        return false;
    }

    void setWebDriverCollection(RemoteWebDriver remoteWebDriver, long waitTimeout, long waitSleep) {
        webDriverCollection.set(new WebDriverCollection(remoteWebDriver, waitTimeout, waitSleep));
    }

    void removeWebDriverCollection() {
        webDriverCollection.remove();
    }

    private WebDriverCollection getCollection() {
        if (webDriverCollection.get() == null)
            throw new IllegalStateException("Web driver collection was not properly set");
        return webDriverCollection.get();
    }

}
