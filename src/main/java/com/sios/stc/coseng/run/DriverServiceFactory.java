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

import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.opera.OperaDriverService;
import org.openqa.selenium.remote.service.DriverService;
import org.openqa.selenium.safari.SafariDriverService;

import com.sios.stc.coseng.run.SeleniumBrowser.Browser;
import com.sios.stc.coseng.util.Stringer;

public final class DriverServiceFactory {

    static DriverService getDriverService(Browser browser, File executable, File logFile) {
        /*
         * CHROME, IE capable of concurrent web driver instantiations which
         * SeleniumWebDriver should set concurrentService and start/stopped from
         * TestNgListener onExecutionStart/onExecutionFinish.
         */
        switch (browser) {
            case FIREFOX:
                if (executable != null)
                    return new GeckoDriverService.Builder().usingDriverExecutable(executable).withLogFile(logFile)
                            .usingAnyFreePort().build();
                else
                    return new GeckoDriverService.Builder().withLogFile(logFile).usingAnyFreePort().build();
            case CHROME:
                if (executable != null)
                    return new ChromeDriverService.Builder().usingDriverExecutable(executable).withLogFile(logFile)
                            .usingAnyFreePort().build();
                else
                    return new ChromeDriverService.Builder().withLogFile(logFile).usingAnyFreePort().build();
            case EDGE:
                if (executable != null)
                    return new EdgeDriverService.Builder().usingDriverExecutable(executable).withLogFile(logFile)
                            .usingAnyFreePort().build();
                else
                    return new EdgeDriverService.Builder().withLogFile(logFile).usingAnyFreePort().build();
            case IE:
                // TODO InternetExplorerDriverService.Builder isn't like the other builders
            case SAFARI:
                if (executable != null)
                    return new SafariDriverService.Builder().usingDriverExecutable(executable).withLogFile(logFile)
                            .usingAnyFreePort().build();
                else
                    return new SafariDriverService.Builder().withLogFile(logFile).usingAnyFreePort().build();
            case OPERA:
                if (executable != null)
                    return new OperaDriverService.Builder().usingDriverExecutable(executable).withLogFile(logFile)
                            .usingAnyFreePort().build();
                else
                    return new OperaDriverService.Builder().withLogFile(logFile).usingAnyFreePort().build();
            default:
                throw new IllegalArgumentException("Unsupported browser " + Stringer.wrapBracket(browser));
        }
    }

}
