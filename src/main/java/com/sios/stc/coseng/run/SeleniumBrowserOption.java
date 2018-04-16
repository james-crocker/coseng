package com.sios.stc.coseng.run;

import java.net.URI;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.safari.SafariOptions;

import com.google.gson.annotations.Expose;
import com.sios.stc.coseng.exceptions.CosengConfigException;
import com.sios.stc.coseng.run.SeleniumBrowser.Browser;
import com.sios.stc.coseng.util.Resource;
import com.sios.stc.coseng.util.Stringer;

public final class SeleniumBrowserOption {

    @Expose
    private URI     optionsJson      = null;
    @Expose
    private URI     capabilitiesJson = null;
    @Expose
    private Integer width            = null;
    @Expose
    private Integer height           = null;
    @Expose
    private Boolean maximize         = null;

    private MutableCapabilities options      = null;
    private ExtraCapabilities   capabilities = null;

    MutableCapabilities getOptions() {
        return options;
    }

    ExtraCapabilities getCapabilities() {
        return capabilities;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public boolean isMaximize() {
        return maximize;
    }

    public Dimension getDimension() {
        /* maximize overrides width and height */
        if (!isMaximize() && getWidth() != null && getHeight() != null && getWidth() > 0 && getHeight() > 0) {
            return new Dimension(getWidth(), getHeight());
        }
        return null;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, "options", "capabilities");
    }

    void validateAndPrepare(Browser browser) {
        try {
            switch (browser) {
                case FIREFOX:
                    if (optionsJson == null)
                        options = new FirefoxOptions();
                    else
                        options = (FirefoxOptions) Resource.getObjectFromJson(optionsJson, FirefoxOptions.class, false);
                    break;
                case CHROME:
                    if (optionsJson == null)
                        options = new ChromeOptions();
                    else
                        options = (ChromeOptions) Resource.getObjectFromJson(optionsJson, ChromeOptions.class, false);
                    break;
                case EDGE:
                    if (optionsJson == null)
                        options = new EdgeOptions();
                    else
                        options = (EdgeOptions) Resource.getObjectFromJson(optionsJson, EdgeOptions.class, false);
                    options.setCapability(InternetExplorerDriver.FORCE_CREATE_PROCESS, true);
                    options.setCapability(InternetExplorerDriver.NATIVE_EVENTS, true);
                    break;
                case IE:
                    if (optionsJson == null)
                        options = new InternetExplorerOptions();
                    else
                        options = (InternetExplorerOptions) Resource.getObjectFromJson(optionsJson,
                                InternetExplorerOptions.class, false);
                    break;
                case SAFARI:
                    if (optionsJson == null)
                        options = new SafariOptions();
                    else
                        options = (SafariOptions) Resource.getObjectFromJson(optionsJson, SafariOptions.class, false);
                    break;
                case OPERA:
                    if (optionsJson == null)
                        options = new OperaOptions();
                    else
                        options = (OperaOptions) Resource.getObjectFromJson(optionsJson, OperaOptions.class, false);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported browser " + Stringer.wrapBracket(browser));

            }
            if (capabilitiesJson != null)
                capabilities = (ExtraCapabilities) Resource.getObjectFromJson(capabilitiesJson,
                        ExtraCapabilities.class);
            if (maximize == null)
                maximize = false;
            /* maximize overrides width and height */
            if (!isMaximize()
                    && ((getWidth() != null && getHeight() == null) || (getWidth() == null && getHeight() != null)
                            || (getWidth() != null && getHeight() != null && (getWidth() <= 0 || getHeight() <= 0))))
                throw new IllegalArgumentException(
                        "Invalid browser width or browser height; if either defined both must be greater > 0");
        } catch (Exception e) {
            throw new CosengConfigException(e);
        }
    }

}
