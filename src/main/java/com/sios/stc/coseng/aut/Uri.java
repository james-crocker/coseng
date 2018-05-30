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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.paulhammant.ngwebdriver.NgWebDriver;
import com.sios.stc.coseng.run.Test;
import com.sios.stc.coseng.util.Stringer;

public final class Uri {

    private Test    test    = null;
    private Log     log     = null;
    private Angular angular = null;

    private enum UriAttr {
        HREF("href"), SRC("src");

        private String attribute = null;

        private UriAttr(String attribute) {
            this.attribute = attribute;
        }

        public String get() {
            return attribute;
        }
    }

    Uri(Test test, Log log, Angular angular) {
        this.test = test;
        this.log = log;
        this.angular = angular;
    }

    public URI getCurrent() {
        try {
            if ((test.getSite().isAngular2App() || test.getSite().isAngularApp())) {
                NgWebDriver ngWebDriver = test.getSelenium().getWebDriverContext().getWebDrivers().getNgWebDriver();
                angular.waitToFinish();
                return new URI(ngWebDriver.getLocationAbsUrl());
            } else {
                RemoteWebDriver webDriver = test.getSelenium().getWebDriverContext().getWebDrivers()
                        .getRemoteWebDriver();
                return new URI(webDriver.getCurrentUrl());
            }
        } catch (URISyntaxException ignore) {
            // do nothing; return empty
        }
        return URI.create(StringUtils.EMPTY);
    }

    private By by = By.xpath("//*[@" + UriAttr.HREF.get() + " or @" + UriAttr.SRC.get() + "]");

    public void findOnRoute() {
        URI onRoute = getCurrent();
        String commonMsg = Log.Prefix.TASK.get() + "Find all "
                + Stringer.wrapBracket(UriAttr.HREF.get() + " " + UriAttr.SRC.get()) + " URL on route "
                + Stringer.wrapBracket(onRoute.toString());
        if (test.getCoseng().getUri().isEnableFind()) {
            log.results(org.apache.logging.log4j.Level.INFO, commonMsg, null, null);
            if (onRoute != null && !onRoute.toString().isEmpty()) {
                RemoteWebDriver webDriver = test.getSelenium().getWebDriverContext().getWebDrivers()
                        .getRemoteWebDriver();
                angular.waitToFinish();
                List<org.openqa.selenium.WebElement> uriList = webDriver.findElements(by);
                for (org.openqa.selenium.WebElement webElement : uriList) {
                    String foundUri = (webElement.getAttribute(UriAttr.HREF.get()) == null
                            ? webElement.getAttribute(UriAttr.SRC.get())
                            : null);
                    test.getCoseng().getUri().getUriFound().put(foundUri, webElement.getTagName(), onRoute);
                }
            }
        } else {
            log.results(org.apache.logging.log4j.Level.WARN, commonMsg + " disabled", null, null);
        }
    }

}
