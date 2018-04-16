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

import com.sios.stc.coseng.runnner.Coseng;
import com.sios.stc.coseng.runnner.Log;
import com.sios.stc.coseng.util.Stringer;

public class Bing extends Coseng {

    private static final Logger log4j = LogManager.getLogger(Bing.class);

    @Test(description = "Verify connect to Bing and search", groups = { "bing1", "all" })
    public void connect1() {
        String searchform = "sb_form_q";
        String url = "https://www.bing.com";

        /* Make sure a web driver for this thread */
        WebDriver webDriver = getWrappedDriver();
        log4j.debug("Test [{}], web driver [{}], thread [{}]", cosengTest.getId(), webDriver.hashCode(),
                Thread.currentThread().getId());
        Log log = cosengLog;

        /* Get the url and assure on correct route. */
        log.testStep("Navigating to url " + Stringer.wrapBracket(url) + " and assuring search form available]");
        webDriver.get(url);

        log.hardAssert.assertTrue(webDriver.getCurrentUrl().contains(url),
                "Current URL should contain " + Stringer.wrapBracket(url));

        /* Get a COSENG WebElement object, find it and assure displayed */
        WebElement weSearchForm = webDriver.findElement(By.id(searchform));
        log.hardAssert.assertTrue(weSearchForm.isDisplayed(), "Search form element should be displayed");

        /* Take a screenshot while were here */
        log.message("Saving screenshot " + Stringer.wrapBracket("bing-connect1"));
        cosengWindow.saveScreenshot("bing-connect1");

        /* Find and save URLs on this route */
        log.message("Finding URIs");
        cosengUri.findOnRoute();
    }

    @Test(description = "Verify connect to Bing Help and search", groups = { "bing2", "all" })
    public void connect2() {
        String searchForm = "//*[@id=\"searchquery\"]";
        String url = "http://help.bing.microsoft.com/#apex/18/en-US/n1999/-1/en-US";

        /* Make sure a web driver for this thread */
        WebDriver webDriver = getWrappedDriver();
        log4j.debug("Test [{}], web driver [{}], thread [{}]", cosengTest.getId(), webDriver.hashCode(),
                Thread.currentThread().getId());
        Log log = cosengLog;

        /* Get the url and assure on correct route. */
        log.testStep("Navigating to url " + Stringer.wrapBracket(url) + " and assuring help button available");
        webDriver.get(url);
        log.hardAssert.assertTrue(webDriver.getCurrentUrl().contains(url),
                "Current URL should contain " + Stringer.wrapBracket(url));

        /* Get a COSENG WebElement object, find it and assure displayed */
        // TODO: Fix
        WebElement weSearchForm = webDriver.findElement(By.xpath(searchForm));
        log.hardAssert.assertTrue(weSearchForm.isDisplayed(), "Search form element should be displayed");

        /* Take a screenshot while were here */
        log.message("Saving screenshot " + Stringer.wrapBracket("bing-connect2"));
        cosengWindow.saveScreenshot("bing-connect2");

        /* Find and save URLs on this route */
        log.message("Finding URIs");
        cosengUri.findOnRoute();
    }

}
