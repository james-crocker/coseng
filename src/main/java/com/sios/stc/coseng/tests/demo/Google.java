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

import java.net.MalformedURLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.sios.stc.coseng.runnner.Coseng;
import com.sios.stc.coseng.runnner.Log;
import com.sios.stc.coseng.util.Stringer;

@Test(groups = { "any" })
public class Google extends Coseng {

    private static final Logger log4j = LogManager.getLogger(Google.class);

    @Test(description = "Verify connect to Google and search", groups = { "g1" })
    @Parameters({ "testCharge" })
    public void connect1(@Optional("negative") String testCharge) throws MalformedURLException {
        String searchForm = "searchform";
        String url = "http://www.google.com";
        String redirectedUrl = "https://www.google.com";

        /* Make sure a web driver for this thread */
        WebDriver webDriver = getWrappedDriver();
        log4j.debug("Test [{}], web driver [{}], thread [{}]", cosengTest.getId(), webDriver.hashCode(),
                Thread.currentThread().getId());
        Log log = cosengLog;

        /* Get the url and assure on correct route. */
        log.testStep("Navigating to url " + Stringer.wrapBracket(url) + " and assuring search form available");
        webDriver.navigate().to(url);

        log.hardAssert.assertTrue(webDriver.getCurrentUrl().startsWith(redirectedUrl),
                "Current URL should contain " + Stringer.wrapBracket(redirectedUrl));

        /* Get a COSENG WebElement object, find it and assure displayed */
        WebElement weSearchForm = webDriver.findElement(By.id(searchForm));
        log.hardAssert.assertTrue(weSearchForm.isDisplayed(), "Search form element should be displayed");

        /* Take a screenshot while were here */
        log.message("Saving screenshot [google-connect1]");
        cosengWindow.saveScreenshot("google-connect1");

        /* Find and save URLs on this route */
        log.message("Finding URLs");
        cosengUri.findOnRoute();
    }

    @Test(description = "Verify connect to Google About and Carrers link")
    public void connect2() {
        String carrers = "//*[@id=\"footer-sitemap-about-content\"]/div/ul/li[3]/a";
        String url = "https://www.google.com/intl/en/about/";

        /* Make sure a web driver for this thread */
        WebDriver webDriver = getWrappedDriver();
        log4j.debug("Test [{}], web driver [{}], thread [{}]", cosengTest.getId(), webDriver.hashCode(),
                Thread.currentThread().getId());
        Log log = cosengLog;

        /* Get the url and assure on correct route. */
        log.testStep("Navigating to url " + Stringer.wrapBracket(url) + " and assuring Carrers links available");
        webDriver.navigate().to(url);

        log.hardAssert.assertTrue(webDriver.getCurrentUrl().contains(url),
                "Current URL should contain " + Stringer.wrapBracket(url));

        /* Get a COSENG WebElement object, find it and assure displayed */
        WebElement weCarrers = webDriver.findElement(By.xpath(carrers));
        log.hardAssert.assertTrue(weCarrers.isDisplayed(), "Carrers web element should be displayed");

        /* Take a screenshot while were here */
        log.message("Saving screenshot " + Stringer.wrapBracket("google-connect2"));
        cosengWindow.saveScreenshot("google-connect2");

        /* Find and save URLs on this route */
        log.message("Finding URIs");
        cosengUri.findOnRoute();
    }

}
