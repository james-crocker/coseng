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

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Platform;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.seleniumhq.selenium.fluent.FluentWebDriver;

import com.paulhammant.ngwebdriver.NgWebDriver;
import com.sios.stc.coseng.util.Stringer;

public final class WebDriverCollection {

    private Platform           platform        = null;
    private String             browserName     = null;
    private String             browserVersion  = null;
    private Map<String, ?>     capabilities    = null;
    private RemoteWebDriver    remoteWebDriver = null;
    private WebDriverWait      webDriverWait   = null;
    private Actions            actions         = null;
    private JavascriptExecutor jsExecutor      = null;
    private NgWebDriver        ngWebDriver     = null;
    private FluentWebDriver    fluentWebDriver = null;

    WebDriverCollection(RemoteWebDriver remoteWebDriver, long waitTimeout, long waitSleep) {
        if (remoteWebDriver == null)
            throw new IllegalArgumentException("Remote web driver must be provided");
        Capabilities caps = remoteWebDriver.getCapabilities();
        if (caps != null) {
            platform = caps.getPlatform();
            browserName = caps.getBrowserName();
            browserVersion = caps.getVersion();
            capabilities = caps.asMap();
        }
        if (browserName == null || browserName.isEmpty())
            browserName = Stringer.UNKNOWN;
        if (browserVersion == null || browserVersion.isEmpty())
            browserVersion = Stringer.UNKNOWN;
        if (platform == null)
            platform = Platform.ANY;
        if (capabilities == null)
            capabilities = new HashMap<String, Object>();

        this.remoteWebDriver = remoteWebDriver;
        webDriverWait = new WebDriverWait(remoteWebDriver, waitTimeout, waitSleep);
        actions = new Actions(remoteWebDriver);
        jsExecutor = (JavascriptExecutor) remoteWebDriver;
        ngWebDriver = new NgWebDriver((JavascriptExecutor) remoteWebDriver);
        fluentWebDriver = new FluentWebDriver(remoteWebDriver);
    }

    String getBrowserName() {
        return browserName;
    }

    String getBrowserVersion() {
        return browserVersion;
    }

    Platform getPlatform() {
        return platform;
    }

    Map<String, ?> getCapabilities() {
        return capabilities;
    }

    RemoteWebDriver getRemoteWebDriver() {
        return remoteWebDriver;
    }

    WebDriverWait getWebDriverWait() {
        return webDriverWait;
    }

    Actions getActions() {
        return actions;
    }

    JavascriptExecutor getJsExecutor() {
        return jsExecutor;
    }

    NgWebDriver getNgWebDriver() {
        return ngWebDriver;
    }

    FluentWebDriver getFluentWebDriver() {
        return fluentWebDriver;
    }

}
