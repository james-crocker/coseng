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
import org.testng.Assert;
import org.testng.annotations.Test;

import com.sios.stc.coseng.run.CosengException;
import com.sios.stc.coseng.run.CosengRunner;
import com.sios.stc.coseng.run.WebElement;

public class Ask extends CosengRunner {

    private static final Logger log = LogManager.getLogger(Ask.class.getName());

    @Test(description = "Verify connect to Ask and search")
    public void connect1() throws CosengException {
        String url = "https://www.ask.com";
        String searchform = "search-box";

        /* Make sure a web driver for this thread */
        Assert.assertTrue(hasWebDriver(), "there should be a web driver");
        log.debug("Test [{}], web driver [{}], thread [{}]", getTest().getName(),
                getWebDriver().hashCode(), Thread.currentThread().getId());

        /*
         * Get the url and assure on correct route. Note: Using the convenience
         * method. Can always get the web driver with WebDriver webDriver =
         * getWebDriver();
         */
        logTestStep("navigating to url [" + url + "] and assuring search form available]");
        webDriverGet(url);
        logAssert.assertTrue(currentUrlContains(url), "current URL should contain [" + url + "]");

        /* Get a COSENG WebElement object, find it and assure displayed */
        WebElement weSearchForm = newWebElement(By.id(searchform));
        weSearchForm.find();
        logAssert.assertTrue(weSearchForm.isDisplayed(), "search form element should be displayed");

        logTestStep("test assortment of soft assertions");
        Boolean[] b1 = { false, false };
        Boolean[] b2 = { true, true };
        Boolean[] b3 = { false, true, false };
        logSoftAssert.assertEquals(b1.length, b3.length);
        logSoftAssert.assertEquals(b1.length, b3.length, "withMessage");
        logSoftAssert.assertEquals(b1.length, b2.length);
        logSoftAssert.assertEquals(b1.length, b2.length, "withMessage");
        logSoftAssert.assertEqualsNoOrder(b1, b2);
        logSoftAssert.assertEqualsNoOrder(b1, b2, "withMessage");
        logSoftAssert.assertEqualsNoOrder(b1, b1);
        logSoftAssert.assertEqualsNoOrder(b1, b1, "withMessage");
        logSoftAssert.assertFalse(true);
        logSoftAssert.assertFalse(true, "withMessage");
        logSoftAssert.assertFalse(false);
        logSoftAssert.assertFalse(false, "withMessage");
        logSoftAssert.assertNotEquals("aa", "aa");
        logSoftAssert.assertNotEquals("aa", "aa", "withMessage");
        logSoftAssert.assertNotEquals("aa", "bb");
        logSoftAssert.assertNotEquals("aa", "bb", "withMessage");
        logSoftAssert.assertNotNull(null);
        logSoftAssert.assertNotNull(null, "withMessage");
        logSoftAssert.assertNotNull("aa");
        logSoftAssert.assertNotNull("aa", "withMessage");
        logSoftAssert.assertNotSame(b1, b1);
        logSoftAssert.assertNotSame(b1, b1, "withMessage");
        logSoftAssert.assertNotSame(b1, b2);
        logSoftAssert.assertNotSame(b1, b2, "withMessage");
        logSoftAssert.assertNull("aa");
        logSoftAssert.assertNull("aa", "withMessage");
        logSoftAssert.assertNull(null);
        logSoftAssert.assertNull(null, "withMessage");
        logSoftAssert.assertSame(b1, b2);
        logSoftAssert.assertSame(b1, b2, "withMessage");
        logSoftAssert.assertSame(b1, b1);
        logSoftAssert.assertSame(b1, b1, "withMessage");
        logSoftAssert.assertTrue(false);
        logSoftAssert.assertTrue(false, "withMessage");
        logSoftAssert.assertTrue(true);
        logSoftAssert.assertTrue(true, "withMessage");

        /* Take a screenshot while were here */
        saveScreenshot("ask-connect1");

        /* Find and save URLs on this route */
        findUrls();
    }

    @Test(description = "Verify connect to Ask About and help button")
    public void connect2() throws CosengException {
        String url = "https://about.ask.com";
        String helpButton = "/html/body/section/aside/button";

        /* Make sure a web driver for this thread */
        Assert.assertTrue(hasWebDriver(), "there should be an available webdriver");
        log.debug("Test [{}], web driver [{}], thread [{}]", getTest().getName(),
                getWebDriver().hashCode(), Thread.currentThread().getId());

        /*
         * Get the url and assure on correct route. Note: Using the convenience
         * method. Can always get the web driver with WebDriver webDriver =
         * getWebDriver();
         */
        logTestStep("navigating to url [" + url + "] and assuring help button available");
        webDriverGet(url);
        logAssert.assertTrue(currentUrlContains(url), "current URL should contain [" + url + "]");

        /* Get a COSENG WebElement object, find it and assure displayed */
        WebElement weHelpButton = newWebElement(By.xpath(helpButton));
        weHelpButton.find();
        logAssert.assertTrue(weHelpButton.isDisplayed(), "help button should be displayed");

        /* Take a screenshot while were here */
        saveScreenshot("ask-connect2");

        /* Find and save URLs on this route */
        findUrls();
        logMessage("a message");
    }

}
