package com.sios.stc.coseng.run;

import java.util.Map;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Platform;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.paulhammant.ngwebdriver.NgWebDriver;

public final class WebDrivers {

    /*
     * https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/
     * ConcurrentHashMap.html
     */
    private static final InheritableThreadLocal<WebDriverCollection> webDriverCollection = new InheritableThreadLocal<WebDriverCollection>();

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

    public org.openqa.selenium.WebDriver getWebDriver() {
        return getCollection().getWebDriver();
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

    void setWebDriverCollection(RemoteWebDriver remoteWebDriver, long waitTimeout, long waitSleep) {
        webDriverCollection.set(new WebDriverCollection(remoteWebDriver, waitTimeout, waitSleep));
    }

    private WebDriverCollection getCollection() {
        if (webDriverCollection.get() == null)
            throw new IllegalStateException("Web driver collection was not properly set");
        return webDriverCollection.get();
    }

}
