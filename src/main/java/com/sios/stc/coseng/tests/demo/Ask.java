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

public class Ask extends Coseng {

    private static final Logger log4j = LogManager.getLogger(Ask.class);

    @Test(description = "Verify connect to Ask and search")
    public void connect1() {
        String url = "https://www.ask.com";
        String searchform = "search-box";

        /* Make sure a web driver for this thread */
        log4j.debug("Test [{}], web driver [{}], thread [{}]", cosengTest.getId(),
                cosengTest.getSelenium().getWebDriverContext().getWebDrivers().getWebDriver().hashCode(),
                Thread.currentThread().getId());
        Log log = cosengLog;

        WebDriver wd = getWrappedDriver();
        /*
         * Get the url and assure on correct route. Note: Using the convenience method.
         * Can always get the web driver with WebDriver webDriver = getWebDriver();
         */
        log.testStep("Navigating to url " + Stringer.wrapBracket(url) + " and assuring search form available]");
        wd.navigate().to(url);
        log.hardAssert.assertTrue(wd.getCurrentUrl().contains(url),
                "Current URL should contain " + Stringer.wrapBracket(url));

        /* Get a COSENG WebElement object, find it and assure displayed */
        WebElement weSearchForm = wd.findElement(By.id(searchform));
        log.hardAssert.assertTrue(weSearchForm.isDisplayed(), "Search form element should be displayed");

        log.testStep("Test assortment of soft assertions");
        Boolean[] b1 = { false, false };
        Boolean[] b2 = { true, true };
        Boolean[] b3 = { false, true, false };
        log.softAssert.assertEquals(b1.length, b3.length);
        log.softAssert.assertEquals(b1.length, b3.length, "withMessage");
        log.softAssert.assertEquals(b1.length, b2.length);
        log.softAssert.assertEquals(b1.length, b2.length, "withMessage");
        log.softAssert.assertEqualsNoOrder(b1, b2);
        log.softAssert.assertEqualsNoOrder(b1, b2, "withMessage");
        log.softAssert.assertEqualsNoOrder(b1, b1);
        log.softAssert.assertEqualsNoOrder(b1, b1, "withMessage");
        log.softAssert.assertFalse(true);
        log.softAssert.assertFalse(true, "withMessage");
        log.softAssert.assertFalse(false);
        log.softAssert.assertFalse(false, "withMessage");
        log.softAssert.assertNotEquals("aa", "aa");
        log.softAssert.assertNotEquals("aa", "aa", "withMessage");
        log.softAssert.assertNotEquals("aa", "bb");
        log.softAssert.assertNotEquals("aa", "bb", "withMessage");
        log.softAssert.assertNotNull(null);
        log.softAssert.assertNotNull(null, "withMessage");
        log.softAssert.assertNotNull("aa");
        log.softAssert.assertNotNull("aa", "withMessage");
        log.softAssert.assertNotSame(b1, b1);
        log.softAssert.assertNotSame(b1, b1, "withMessage");
        log.softAssert.assertNotSame(b1, b2);
        log.softAssert.assertNotSame(b1, b2, "withMessage");
        log.softAssert.assertNull("aa");
        log.softAssert.assertNull("aa", "withMessage");
        log.softAssert.assertNull(null);
        log.softAssert.assertNull(null, "withMessage");
        log.softAssert.assertSame(b1, b2);
        log.softAssert.assertSame(b1, b2, "withMessage");
        log.softAssert.assertSame(b1, b1);
        log.softAssert.assertSame(b1, b1, "withMessage");
        log.softAssert.assertTrue(false);
        log.softAssert.assertTrue(false, "withMessage");
        log.softAssert.assertTrue(true);
        log.softAssert.assertTrue(true, "withMessage");

        /* Take a screenshot while were here */
        log.message("Saving screenshot " + Stringer.wrapBracket("ask-connect1"));
        cosengWindow.saveScreenshot("ask-connect1");

        /* Find and save URLs on this route */
        log.message("Finding URIs");
        cosengUri.findOnRoute();
    }

    @Test(description = "Verify connect to Ask About and help button")
    public void connect2() {
        String url = "https://about.ask.com";
        String helpButton = "/html/body/section/aside/button";

        /* Make sure a web driver for this thread */
        log4j.debug("Test [{}], web driver [{}], thread [{}]", cosengTest.getId(),
                cosengTest.getSelenium().getWebDriverContext().getWebDrivers().getWebDriver().hashCode(),
                Thread.currentThread().getId());
        Log log = cosengLog;

        WebDriver wd = getWrappedDriver();

        /*
         * Get the url and assure on correct route. Note: Using the convenience method.
         * Can always get the web driver with WebDriver webDriver = getWebDriver();
         */
        log.testStep("Navigating to url " + Stringer.wrapBracket(url) + " and assuring help button available");
        wd.navigate().to(url);
        log.hardAssert.assertTrue(wd.getCurrentUrl().contains(url),
                "current URL should contain " + Stringer.wrapBracket(url));

        /* Get a COSENG WebElement object, find it and assure displayed */
        WebElement weHelpButton = wd.findElement(By.xpath(helpButton));
        log.hardAssert.assertTrue(weHelpButton.isDisplayed(), "Help button should be displayed");

        /* Take a screenshot while were here */
        log.message("Saving screenshot " + Stringer.wrapBracket("ask-connect2"));
        cosengWindow.saveScreenshot("ask-connect2");

        /* Find and save URLs on this route */
        log.message("Finding URIs");
        cosengUri.findOnRoute();
    }

}
