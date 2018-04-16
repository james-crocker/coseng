package com.sios.stc.coseng.run;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.paulhammant.ngwebdriver.NgWebDriver;
import com.sios.stc.coseng.util.Stringer;

public final class WebDriverCollection {

    private Platform           platform       = null;
    private String             browserName    = null;
    private String             browserVersion = null;
    private Map<String, ?>     capabilities   = null;
    private WebDriver          webDriver      = null;
    private WebDriverWait      webDriverWait  = null;
    private Actions            actions        = null;
    private JavascriptExecutor jsExecutor     = null;
    private NgWebDriver        ngWebDriver    = null;

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

        webDriver = remoteWebDriver;
        webDriverWait = new WebDriverWait(remoteWebDriver, waitTimeout, waitSleep);
        actions = new Actions(remoteWebDriver);
        jsExecutor = (JavascriptExecutor) remoteWebDriver;
        ngWebDriver = new NgWebDriver((JavascriptExecutor) remoteWebDriver);
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

    org.openqa.selenium.WebDriver getWebDriver() {
        return webDriver;
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

}
