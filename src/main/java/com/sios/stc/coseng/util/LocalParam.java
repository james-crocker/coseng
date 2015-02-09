/*
 * This file is part of COSENG (Concurrent Selenium TestNG Runner).
 * 
 * Copyright (c) 2015 SIOS Technology Corp. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sios.stc.coseng.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocalParam {
    private final String testReportDirectory = null;
    private Boolean jarSuiteOverwrite = null;
    private final String jarSuiteDirectory = null;
    private final String chromeDriver = null;
    private final String ieDriver = null;

    private File localChromeDriver = null;
    private File localIeDriver = null;
    private File localJarSuiteDirectory = null;
    private File localTestReportDirectory = null;

    private static final Logger log = Logger.getLogger(LocalParam.class.getName());

    LocalParam() throws IOException {
        // do nothing
    }

    public synchronized void initialize() {
        if ((chromeDriver != null) && !chromeDriver.isEmpty()) {
            localChromeDriver = new File(chromeDriver);
        }
        if ((ieDriver != null) && !ieDriver.isEmpty()) {
            localIeDriver = new File(ieDriver);
        }
        if ((testReportDirectory == null) || testReportDirectory.isEmpty()) {
            LocalParam.log
            .log(Level.INFO,
                    "testReportDirectory was not provided; test reports will be saved in the current working directory");
            localTestReportDirectory = new File("");
        } else {
            localTestReportDirectory = new File(
                    testReportDirectory);
        }
        if ((jarSuiteDirectory == null) || jarSuiteDirectory.isEmpty()) {
            LocalParam.log
            .log(Level.INFO,
                    "jarSuiteDirectory was not provided; jar suite xml will be saved in the current working directory");
            localJarSuiteDirectory = new File("");
        } else {
            localJarSuiteDirectory = new File(jarSuiteDirectory);
        }
        if (jarSuiteOverwrite == null) {
            LocalParam.log
            .log(Level.INFO,
                    "jarSuiteOverwirte not provided; assuming (true). If executed from *.jar; copied suite files in jarSuiteDirectory will be overwritten for each test run");
            jarSuiteOverwrite = true;
        }
    }

    public synchronized File getTestReportDirectory() {
        return localTestReportDirectory;
    }

    public synchronized void setTestReportDirectory(final File directory) {
        if (directory.isDirectory()) {
            localTestReportDirectory = directory;
        }
    }

    public synchronized File getJarSuiteDirectory() {
        return localJarSuiteDirectory;
    }

    public synchronized Boolean isJarSuiteOverwrite() {
        return jarSuiteOverwrite;
    }

    public synchronized File getIeDriver() {
        return localIeDriver;
    }

    public synchronized File getChromeDriver() {
        return localChromeDriver;
    }

    @Override
    public synchronized String toString() {
        return "testReportDirectory ("
                + localTestReportDirectory
                + "), jarSuiteDirectory ("
                + localJarSuiteDirectory
                + "), jarSuiteOverwrite ("
                + jarSuiteOverwrite
                + "), chromeDriver ("
                + ((chromeDriver == null) || chromeDriver.isEmpty() ? "unset"
                        : localChromeDriver)
                        + "), ieDriver ("
                        + ((ieDriver == null) || ieDriver.isEmpty() ? "unset"
                                : localIeDriver) + ")";
    }
}
