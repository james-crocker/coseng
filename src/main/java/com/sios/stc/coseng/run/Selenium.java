package com.sios.stc.coseng.run;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.google.gson.annotations.Expose;

public final class Selenium {

    @Expose
    private SeleniumBrowser   browser   = null;
    @Expose
    private SeleniumWebDriver webDriver = null;

    public SeleniumBrowser getBrowser() {
        return browser;
    }

    public SeleniumWebDriver getWebDriverContext() {
        return webDriver;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    void validateAndPrepare(Test test) {
        if (browser == null)
            browser = new SeleniumBrowser();
        browser.validateAndPrepare();
        if (webDriver == null)
            webDriver = new SeleniumWebDriver();
        webDriver.validateAndPrepare(test);
        /* Revalidate browser executable; depends webDriver */
        browser.validateExecutable(webDriver.getLocation());
    }

}
