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
package com.sios.stc.coseng.tests.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.sios.stc.coseng.run.SeleniumBrowser.Browser;
import com.sios.stc.coseng.runnner.Coseng;
import com.sios.stc.coseng.runnner.Log;
import com.sios.stc.coseng.util.Stringer;

public class DuckDuckGo extends Coseng {

    private static final Logger log4j = LogManager.getLogger(DuckDuckGo.class);

    @Test(description = "Verify connect to DuckDuckGo and search")
    public void connect1() {
        String searchForm = "search_form_input_homepage";
        String url = "https://www.duckduckgo.com";
        String redirectedUrl = "https://duckduckgo.com";

        /* Make sure a web driver for this thread */
        log4j.debug("Test [{}], web driver [{}], thread [{}]", cosengTest.getId(),
                cosengTest.getSelenium().getWebDriverContext().getWebDrivers().getWebDriver().hashCode(),
                Thread.currentThread().getId());
        Log log = cosengLog;

        Browser browser = cosengTest.getSelenium().getBrowser().getType();

        WebDriver wd = getWrappedDriver();

        /*
         * Get the url and assure on correct route. Note: Using the convenience method.
         * Can always get the web driver with WebDriver webDriver = getWebDriver();
         */
        log.testStep("Navigating to url " + Stringer.wrapBracket(url) + " and assuring search form available");

        wd.navigate().to(url);
        log.hardAssert.assertTrue(wd.getCurrentUrl().contains(redirectedUrl),
                "Current URL contains " + Stringer.wrapBracket(redirectedUrl));

        /* Get a COSENG WebElement object, find it and assure displayed */
        WebElement weSearchForm = wd.findElement(By.id(searchForm));
        if (!Browser.EDGE.equals(browser)) {
            log.hardAssert.assertTrue(weSearchForm.isDisplayed(), "Search form element should be displayed");
        } else {
            log.skipTestForBrowser();
        }

        /* Take a screenshot while were here */
        log.message("Saving screenshot " + Stringer.wrapBracket("duckDuckGo-connect1"));
        cosengWindow.saveScreenshot("duckDuckGo-connect1");

        /* Find and save URLs on this route */
        log.message("Finding URIs");
        cosengUri.findOnRoute();
    }

}
