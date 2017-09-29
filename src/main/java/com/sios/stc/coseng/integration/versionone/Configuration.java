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
package com.sios.stc.coseng.integration.versionone;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.annotations.Expose;

/**
 * The Class Configuration.
 *
 * @since 3.0
 * @version.coseng
 */
public class Configuration {

    @Expose
    private final String  version         = null;
    @Expose
    private final String  instanceUrl     = null;
    @Expose
    private final String  accessToken     = null;
    @Expose
    private final String  applicationName = null;
    @Expose
    private final String  projectName     = null;
    @Expose
    private final String  sprintName      = null;
    @Expose
    private final Backlog backlog         = null;
    @Expose
    private final Test    test            = null;

    /**
     * Gets the version.
     *
     * @return the version
     * @since 3.0
     * @version.coseng
     */
    public String getVersion() {
        if (version != null) {
            return version;
        }
        return StringUtils.EMPTY;
    }

    /**
     * Gets the instance url.
     *
     * @return the instance url
     * @since 3.0
     * @version.coseng
     */
    public String getInstanceUrl() {
        if (instanceUrl != null) {
            return instanceUrl;
        }
        return StringUtils.EMPTY;
    }

    /**
     * Gets the access token.
     *
     * @return the access token
     * @since 3.0
     * @version.coseng
     */
    public String getAccessToken() {
        if (accessToken != null) {
            return accessToken;
        }
        return StringUtils.EMPTY;
    }

    /**
     * Gets the application name.
     *
     * @return the application name
     * @since 3.0
     * @version.coseng
     */
    public String getApplicationName() {
        if (applicationName != null) {
            return applicationName;
        }
        return StringUtils.EMPTY;
    }

    /**
     * Gets the project name.
     *
     * @return the project name
     * @since 3.0
     * @version.coseng
     */
    public String getProjectName() {
        if (projectName != null) {
            return projectName;
        }
        return StringUtils.EMPTY;
    }

    /**
     * Gets the sprint name.
     *
     * @return the sprint name
     * @since 3.0
     * @version.coseng
     */
    public String getSprintName() {
        if (sprintName != null) {
            return sprintName;
        }
        return StringUtils.EMPTY;
    }

    /**
     * Gets the backlog.
     *
     * @return the backlog
     * @since 3.0
     * @version.coseng
     */
    public Backlog getBacklog() {
        if (backlog != null) {
            return backlog;
        }
        return new Backlog();
    }

    /**
     * Gets the test.
     *
     * @return the test
     * @since 3.0
     * @version.coseng
     */
    public Test getTest() {
        if (test != null) {
            return test;
        }
        return new Test();
    }

}
