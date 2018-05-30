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
package com.sios.stc.coseng.integration.versionone;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.testng.IInvokedMethod;

import com.google.gson.annotations.Expose;

/**
 * The Class Configuration.
 *
 * @since 3.0
 * @version.coseng
 */
public final class VersionOne {

    @Expose
    private String  version         = null;
    @Expose
    private URL     instanceUrl     = null;
    @Expose
    private String  accessToken     = null;
    @Expose
    private String  applicationName = null;
    @Expose
    private String  projectName     = null;
    @Expose
    private String  sprintName      = null;
    @Expose
    private Backlog backlog         = null;
    @Expose
    private Test    test            = null;

    private Map<IInvokedMethod, MethodTest> methodsTest = new ConcurrentHashMap<IInvokedMethod, MethodTest>();

    /**
     * Gets the version.
     *
     * @return the version
     * @since 3.0
     * @version.coseng
     */
    public String getVersion() {
        return version;
    }

    /**
     * Gets the instance url.
     *
     * @return the instance url
     * @since 3.0
     * @version.coseng
     */
    public URL getInstanceUrl() {
        return instanceUrl;
    }

    /**
     * Gets the access token.
     *
     * @return the access token
     * @since 3.0
     * @version.coseng
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Gets the application name.
     *
     * @return the application name
     * @since 3.0
     * @version.coseng
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * Gets the project name.
     *
     * @return the project name
     * @since 3.0
     * @version.coseng
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Gets the sprint name.
     *
     * @return the sprint name
     * @since 3.0
     * @version.coseng
     */
    public String getSprintName() {
        return sprintName;
    }

    /**
     * Gets the backlog.
     *
     * @return the backlog
     * @since 3.0
     * @version.coseng
     */
    public Backlog getBacklog() {
        return backlog;
    }

    public MethodTest getMethodTest(IInvokedMethod method) {
        return methodsTest.get(method);
    }

    /**
     * Gets the test.
     *
     * @return the test
     * @since 3.0
     * @version.coseng
     */
    Test getTest() {
        return test;
    }

    void putMethodTest(IInvokedMethod method, MethodTest methodTest) {
        /* Merge the v1 Test fields which apply to all MethodTest */
        methodTest.addFields(test.getFields());
        methodsTest.put(method, methodTest);
    }

    void removeMethodTest(IInvokedMethod method) {
        methodsTest.remove(method);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, "methodsTest");
    }

}
