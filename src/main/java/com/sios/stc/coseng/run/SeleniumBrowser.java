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
package com.sios.stc.coseng.run;

import java.io.File;
import java.net.URI;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.safari.SafariOptions;

import com.google.gson.annotations.Expose;
import com.sios.stc.coseng.exceptions.CosengConfigException;
import com.sios.stc.coseng.run.SeleniumWebDriver.Location;
import com.sios.stc.coseng.util.Resource;
import com.sios.stc.coseng.util.Stringer;

public final class SeleniumBrowser {

    @Expose
    private Browser type        = null;
    @Expose
    private URI     optionsJson = null;
    @Expose
    private File    executable  = null;
    @Expose
    private Integer width       = null;
    @Expose
    private Integer height      = null;
    @Expose
    private Boolean maximize    = null;

    private MutableCapabilities options = null;

    public enum Browser {
        FIREFOX, CHROME, EDGE, IE, SAFARI, OPERA;
    }

    public Browser getType() {
        return type;
    }

    public MutableCapabilities getOptions() {
        return options;
    }

    public File getExecutable() {
        return executable;
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
        return ReflectionToStringBuilder.toStringExclude(this, "options");
    }

    void validateAndPrepare() {
        try {
            if (type == null)
                throw new IllegalArgumentException("Field browser must be provided; supported browsers "
                        + Stringer.wrapBracket(StringUtils.join(Browser.values(), Stringer.Separator.LIST.get())));

            switch (type) {
                case FIREFOX:
                    options = (FirefoxOptions) Resource.getSeleniumBrowserOptionsObjectFromJson(optionsJson,
                            FirefoxOptions.class);
                    break;
                case CHROME:
                    options = (ChromeOptions) Resource.getSeleniumBrowserOptionsObjectFromJson(optionsJson,
                            ChromeOptions.class);
                    break;
                case EDGE:
                    options = (EdgeOptions) Resource.getSeleniumBrowserOptionsObjectFromJson(optionsJson,
                            EdgeOptions.class);
                    break;
                case IE:
                    options = (InternetExplorerOptions) Resource.getSeleniumBrowserOptionsObjectFromJson(optionsJson,
                            InternetExplorerOptions.class);
                    break;
                case SAFARI:
                    options = (SafariOptions) Resource.getSeleniumBrowserOptionsObjectFromJson(optionsJson,
                            SafariOptions.class);
                    break;
                case OPERA:
                    options = (OperaOptions) Resource.getSeleniumBrowserOptionsObjectFromJson(optionsJson,
                            OperaOptions.class);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported browser " + Stringer.wrapBracket(type));

            }

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

    void validateExecutable(Location location) {
        try {
            if (executable != null && Location.NODE.equals(location) && !executable.canExecute())
                throw new IllegalArgumentException("Browser " + Stringer.wrapBracket(type) + " executable "
                        + Stringer.wrapBracket(executable) + " must exist and be executable");
        } catch (Exception e) {
            throw new CosengConfigException(e);
        }
    }

}
