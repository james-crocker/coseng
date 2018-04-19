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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IInvokedMethod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sios.stc.coseng.Triggers.TestPhase;
import com.sios.stc.coseng.Triggers.TriggerOn;
import com.sios.stc.coseng.integration.Integrator;
import com.sios.stc.coseng.integration.versionone.Common.Separator;
import com.sios.stc.coseng.integration.versionone.Common.V1Asset;
import com.sios.stc.coseng.integration.versionone.Common.V1Attr;
import com.sios.stc.coseng.run.Test;
import com.sios.stc.coseng.util.Resource;
import com.sios.stc.coseng.util.Stringer;
import com.versionone.Oid;
import com.versionone.apiclient.Asset;
import com.versionone.apiclient.Query;
import com.versionone.apiclient.Services;
import com.versionone.apiclient.V1Connector;
import com.versionone.apiclient.exceptions.V1Exception;
import com.versionone.apiclient.interfaces.IAssetType;
import com.versionone.apiclient.interfaces.IAttributeDefinition;
import com.versionone.apiclient.interfaces.IServices;
import com.versionone.apiclient.services.QueryResult;

/**
 * The Class VersionOne.
 *
 * @since 3.0
 * @version.coseng
 */
public final class VersionOneIntegrator extends Integrator {

    private static final Logger log = LogManager.getLogger(VersionOneIntegrator.class);

    private static final String INTEGRATOR_NAME = "VersionOne";

    private Test       test = null;
    private VersionOne v1   = null;

    private V1Connector connector  = null;
    private IServices   iServices  = null;
    private Oid         projectOid = null;
    private Oid         sprintOid  = null;
    private Oid         backlogOid = null;
    private IAssetType  iBacklog   = null;
    private IAssetType  iTest      = null;

    private boolean hasUpdatedBacklogDescription = false;

    public VersionOneIntegrator() {
        // do nothing; for allowing access for newInstance() in TestNg
    }

    public VersionOne getVersionOne() {
        return v1;
    }

    @Override
    public void validateAndPrepare(Test test, URI v1Resource) {
        v1 = (VersionOne) Resource.getObjectFromJson(v1Resource, VersionOneJsonDeserializer.getStatic(),
                VersionOne.class);
        /* Validate the connector */
        if (test == null || v1 == null || v1.getVersion() == null || v1.getInstanceUrl() == null
                || v1.getAccessToken() == null || v1.getApplicationName() == null || v1.getProjectName() == null
                || v1.getSprintName() == null || v1.getBacklog() == null || v1.getTest() == null
                || !hasRequiredFields())
            throw new IllegalArgumentException("Configuration parameters misconfigured");
        String version = v1.getVersion();
        URL instanceUrl = v1.getInstanceUrl();
        String accessToken = v1.getAccessToken();
        String applicationName = v1.getApplicationName();
        String projectName = v1.getProjectName();
        String sprintName = v1.getSprintName();
        try {
            connector = V1Connector.withInstanceUrl(instanceUrl.toString())
                    .withUserAgentHeader(applicationName, version).withAccessToken(accessToken).build();
            iServices = new Services(connector);
            iBacklog = iServices.getMeta().getAssetType(V1Asset.BACKLOG.get());
            iTest = iServices.getMeta().getAssetType(V1Asset.TEST.get());
        } catch (MalformedURLException | V1Exception | NullArgumentException e) {
            throw new IllegalStateException("Unable to connect to " + INTEGRATOR_NAME + " "
                    + Stringer.wrapBracket(instanceUrl) + ", application " + Stringer.wrapBracket(applicationName)
                    + ", project " + Stringer.wrapBracket(projectName) + ", sprint " + Stringer.wrapBracket(sprintName),
                    e);
        }
        try {
            projectOid = getProjectOid(projectName);
            sprintOid = getSprintOid(sprintName);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Unable to get " + INTEGRATOR_NAME + " project " + Stringer.wrapBracket(projectName)
                            + " OID and/or sprint " + Stringer.wrapBracket(sprintName) + " OID",
                    e);
        }
        this.test = test;
    }

    @Override
    public void actOn(TriggerOn trigger, TestPhase phase) {
        if (trigger == null || phase == null)
            return;
        switch (phase) {
            case START:
                switch (trigger) {
                    case TESTNGEXECUTION:
                        onExecutionStart();
                        break;
                    case TESTNGMETHOD:
                        onMethodStart();
                        break;
                    default:
                        // do nothing; SUITE, TEST, CLASS unused
                }
                break;
            case FINISH:
                switch (trigger) {
                    case TESTNGEXECUTION:
                        onExecutionFinish();
                        break;
                    case TESTNGMETHOD:
                        onMethodFinish();
                        break;
                    case COSENG:
                        onCosengFinish();
                    default:
                        // do nothing; SUITE, TEST, CLASS unused
                }
                break;
            default:
                // do nothing
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sios.stc.coseng.integration.Integrator#addTestStep(com.sios.stc.
     * coseng.run.Test, java.lang.String)
     */
    @Override
    public void addTestStep(String stepMessage) {
        try {
            MethodTest methodTest = v1.getMethodTest(test.getTestNg().getContext().getIInvokedMethod());
            methodTest.addStep(stepMessage);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to add test step", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sios.stc.coseng.integration.Integrator#addTestStepExpectedResult(com.
     * sios.stc.coseng.run.Test, java.lang.String, java.lang.String)
     */
    @Override
    public void addTestStepExpectedResult(String stepMessage, String expectedResult) {
        try {
            MethodTest methodTest = v1.getMethodTest(test.getTestNg().getContext().getIInvokedMethod());
            methodTest.addStepExpectedResult(stepMessage, expectedResult);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to add test step expected result", e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sios.stc.coseng.integration.Integrator#addTestStepActualResult(com.
     * sios.stc.coseng.run.Test, java.lang.String, java.lang.String)
     */
    @Override
    public void addTestStepActualResult(String stepMessage, String actualResult) {
        try {
            MethodTest methodTest = v1.getMethodTest(test.getTestNg().getContext().getIInvokedMethod());
            methodTest.addStepActualResult(stepMessage, actualResult);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to add test step actual result", e);
        }

    }

    private void onExecutionStart() {
        try {
            TriggerOn trigger = TriggerOn.TESTNGEXECUTION;
            TestPhase phase = TestPhase.START;
            log.debug("TriggerOn [{}], phase [{}], thread [{}], test [{}], testHashCode [{}]", trigger, phase,
                    Thread.currentThread().getId(), test.getId(), test.hashCode());

            v1.getBacklog().setTimebox(sprintOid.getToken());
            v1.getBacklog().setName(v1.getBacklog().getNamePrefix() + test.getId());
            v1.getBacklog().setDescription(Stringer.htmlBold("Test configuration: ") + test.toString());
            /*
             * Create a new backlog. Does not need synchronization as this trigger/phase is
             * single threaded.
             */
            Asset backlog = iServices.createNew(iBacklog, projectOid);
            setFields(iBacklog, backlog, v1.getBacklog().getFields(trigger, phase));
            iServices.save(backlog);
            /* Important to get Oid after save. Oid is null prior to save. */
            backlogOid = backlog.getOid();
        } catch (Exception e) {
            throw new RuntimeException("Unable to create backlog", e);
        }
    }

    private void onExecutionFinish() {
        try {
            TriggerOn trigger = TriggerOn.TESTNGEXECUTION;
            TestPhase phase = TestPhase.FINISH;
            log.debug("TriggerOn [{}], phase [{}], thread [{}], test [{}], testHashCode [{}]", trigger, phase,
                    Thread.currentThread().getId(), test.getId(), test.hashCode());
            attachOutput();
        } catch (Exception e) {
            throw new RuntimeException("Unable to update backlog", e);
        }
    }

    private void onCosengFinish() {
        try {
            TriggerOn trigger = TriggerOn.COSENG;
            TestPhase phase = TestPhase.FINISH;
            log.debug("TriggerOn [{}], phase [{}], thread [{}], test [{}], testHashCode [{}]", trigger, phase,
                    Thread.currentThread().getId(), test.getId(), test.hashCode());

            List<String> updatedDescription = new ArrayList<String>();
            String currentDescription = v1.getBacklog().getDescription();

            updatedDescription.add(Stringer.htmlBold("Test completed ")
                    + new Stringer((test.isFailed() ? "with failures" : "successfully")).htmlItalic().htmlBold()
                            .toString()
                    + "; elapsed time (hh:mm:ss:ms): " + test.getStopWatch().toString() + "; web driver started "
                    + Stringer.wrapBracket(test.getSelenium().getWebDriverContext().getStartedWebDrivers())
                    + ", stopped "
                    + Stringer.wrapBracket(test.getSelenium().getWebDriverContext().getStoppedWebDrivers()));
            updatedDescription.add(currentDescription);
            v1.getBacklog().setDescription(StringUtils.join(updatedDescription, Stringer.Html.LINE_BREAK.get()));

            /* Get this trigger/phase fields and *add* the pass|fail fields. */
            List<Field> fields = v1.getBacklog().getFields(trigger, phase);
            if (test.isFailed()) {
                log.debug("all testing [{}] has failures", test.getId());
                fields.addAll(v1.getBacklog().getFields(trigger, TestPhase.FAIL));
            } else {
                log.debug("all testing [{}] passed", test.getId());
                fields.addAll(v1.getBacklog().getFields(trigger, TestPhase.PASS));
            }

            /*
             * Update backlog. Does not need synchronization as this trigger/phase is single
             * threaded.
             */
            Asset backlog = getBacklogAsset();
            setFields(iBacklog, backlog, fields);
            iServices.save(backlog);
        } catch (Exception e) {
            throw new RuntimeException("Unable to update backlog", e);
        }
    }

    private void onMethodStart() {
        try {
            TriggerOn trigger = TriggerOn.TESTNGMETHOD;
            TestPhase phase = TestPhase.START;
            IInvokedMethod method = test.getTestNg().getContext().getIInvokedMethod();

            log.debug(
                    "TriggerOn [{}], phase [{}], thread [{}], test [{}], testHashCode [{}], method [{}], methodHashCode [{}]",
                    trigger, phase, Thread.currentThread().getId(), test.getId(), test.hashCode(),
                    method.getTestMethod().getQualifiedName(), method.hashCode());

            MethodTest methodTest = new MethodTest();
            String browserVersion = null;
            List<String> webDriverDetails = new ArrayList<String>();

            /* Not all tests may start a web driver; see useWebDriver in TestNgContext. */
            if (test.getSelenium().getWebDriverContext().getWebDrivers().hasWebDriverCollection()) {
                browserVersion = Stringer
                        .wrapBracket(test.getSelenium().getWebDriverContext().getWebDrivers().getPlatform() + ", "
                                + test.getSelenium().getWebDriverContext().getWebDrivers().getBrowserName()
                                + StringUtils.EMPTY + Stringer.wrapParentheses(
                                        test.getSelenium().getWebDriverContext().getWebDrivers().getBrowserVersion()));

                webDriverDetails.add(Stringer.htmlBold("Web driver browser: ") + Stringer.htmlItalic(browserVersion));
                Map<String, ?> webDriverCapabilities = test.getSelenium().getWebDriverContext().getWebDrivers()
                        .getWebDriverCapabilities();
                if (webDriverCapabilities != null) {
                    webDriverDetails.add(Stringer.htmlBold("Web driver capabilities: ")
                            + Stringer.htmlItalic(webDriverCapabilities));
                }
            }

            /*
             * Update the backlog name to include the browser details used for test.
             * Synchronize to avoid threads from duplicating fields.
             */
            synchronized (this) {
                if (!hasUpdatedBacklogDescription && browserVersion != null) {

                    v1.getBacklog().setName(v1.getBacklog().getName() + StringUtils.SPACE + browserVersion);

                    List<String> descriptions = new ArrayList<String>();
                    String currentDescription = v1.getBacklog().getDescription();
                    descriptions.add(currentDescription);
                    descriptions.addAll(webDriverDetails);
                    v1.getBacklog().setDescription(StringUtils.join(descriptions, Stringer.Html.LINE_BREAK.get()));

                    Asset backlog = getBacklogAsset();
                    setFields(iBacklog, backlog, v1.getBacklog().getFields(trigger, phase));
                    iServices.save(backlog);

                    hasUpdatedBacklogDescription = true;
                }
            }

            /* Populate the setup. */
            if (!webDriverDetails.isEmpty())
                methodTest.setSetup(StringUtils.join(webDriverDetails, Stringer.Html.LINE_BREAK.get()));

            /* Set the Test asset w/ test details. */
            String suiteName = test.getTestNg().getContext().getISuite().getName();
            String testName = test.getTestNg().getContext().getITestContext().getName();
            String className = test.getTestNg().getContext().getITestClass().getName();
            String methodName = method.getTestMethod().getMethodName();
            String description = method.getTestMethod().getDescription();

            /* Set test name. */
            methodTest.setName(suiteName + Separator.NAME.get() + methodName
                    + (description != null && !description.isEmpty() ? Separator.NAME.get() + description
                            : StringUtils.EMPTY));

            List<String> descriptions = new ArrayList<String>();
            descriptions.add(Stringer.htmlBold("Suite: ") + Stringer.htmlItalic(suiteName));
            descriptions.add(Stringer.htmlBold("Test: ") + Stringer.htmlItalic(testName));
            descriptions.add(Stringer.htmlBold("Class: ") + Stringer.htmlItalic(className));
            descriptions.add(Stringer.htmlBold("Method: ") + Stringer.htmlItalic(methodName));
            descriptions.add(Stringer.htmlBold("Description: ") + Stringer.htmlItalic(description));
            methodTest.setDescription(StringUtils.join(descriptions, Stringer.Html.LINE_BREAK.get()));

            /* Set Test inputs. */
            methodTest.setInputs(getTestInputParameters());

            /* Add matched TestParamFields. */
            methodTest.addFields(getTestParamFields(trigger, phase));

            v1.putMethodTest(method, methodTest);

            /* Synchronize to avoid threads from duplicating fields. */
            synchronized (this) {
                Asset v1Test = iServices.createNew(iTest, backlogOid);
                setFields(iTest, v1Test, v1.getMethodTest(method).getFields(trigger, phase));
                iServices.save(v1Test);
                /* !! Important to get *after* save; otherwise "NULL" !! */
                v1.getMethodTest(method).setOid(v1Test.getOid());
            }
        } catch (Exception e) {
            /* Hidden by onMethodFinish() in TestNg Report; log */
            String msg = "Unable to create test";
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    private void onMethodFinish() {
        try {
            TriggerOn trigger = TriggerOn.TESTNGMETHOD;
            TestPhase phase = TestPhase.FINISH;
            IInvokedMethod method = test.getTestNg().getContext().getIInvokedMethod();
            com.versionone.Oid testOid = v1.getMethodTest(method).getOid();
            log.debug(
                    "TriggerOn [{}], phase [{}], thread [{}], test [{}], testHashCode [{}], method [{}], methodHashCode [{}], testOid [{}]",
                    trigger, phase, Thread.currentThread().getId(), test.getId(), test.hashCode(),
                    method.getTestMethod().getQualifiedName(), method.hashCode(), testOid);

            /* Get this trigger/phase fields and *add* the pass|fail fields. */
            List<Field> fields = v1.getMethodTest(method).getFields(trigger, phase);
            if (method.getTestResult().isSuccess()) {
                log.debug("Test [{}] method [{}] passed", test.getId(), method.getTestMethod().getMethodName());
                fields.addAll(v1.getMethodTest(method).getFields(trigger, TestPhase.PASS));
            } else {
                log.debug("Test [{}] method [{}] failed", test.getId(), method.getTestMethod().getMethodName());
                fields.addAll(v1.getMethodTest(method).getFields(trigger, TestPhase.FAIL));
            }

            /* Synchronize to avoid threads from duplicating fields. */
            synchronized (this) {
                Query query = new Query(testOid);
                /* Adding the 'status' is required; otherwise no results */
                IAttributeDefinition status = iTest.getAttributeDefinition(V1Attr.STATUS.get());
                query.getSelection().add(status);
                QueryResult result = iServices.retrieve(query);
                Asset v1Test = result.getAssets()[0];
                setFields(iTest, v1Test, fields);
                iServices.save(v1Test);
            }
            v1.removeMethodTest(method);
        } catch (Exception e) {
            throw new RuntimeException("Unable to update test", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sios.stc.coseng.integration.Integrator#attachReports(com.sios.stc.
     * coseng.run.Test, java.io.File, java.io.File)
     */
    private void attachOutput() {
        try {
            /* Attach TestNG reports */
            Asset backlog = getBacklogAsset();
            Path sourcePath = Paths.get(test.getTestNg().getDirectory().getOutput());
            String zipFile = File.createTempFile(test.getId(), ".zip").getAbsolutePath();
            Path zipPath = Paths.get(zipFile);
            Resource.zipFolder(sourcePath, zipPath);
            iServices.saveAttachment(zipFile, backlog, "COSENG Output");
        } catch (Exception e) {
            throw new RuntimeException("Unable to attach COSENG output", e);
        }
    }

    private Asset getBacklogAsset() {
        try {
            Query query = new Query(backlogOid);
            /* Adding the 'status' is required; otherwise no results */
            IAttributeDefinition status = iBacklog.getAttributeDefinition(V1Attr.STATUS.get());
            query.getSelection().add(status);
            QueryResult result = iServices.retrieve(query);
            Asset backlog = result.getAssets()[0];
            return backlog;
        } catch (Exception e) {
            throw new RuntimeException("Unable to query for backlog", e);
        }
    }

    private Oid getProjectOid(String project) {
        String query = "{" + " \"from\": \"Scope\"," + " \"select\": [\"ID\"], \"where\": { \"Name\": \"" + project
                + "\"}" + "}";
        return getOidFromQuery(query);
    }

    private Oid getSprintOid(String sprint) {
        String query = "{" + " \"from\": \"Timebox\","
                + " \"select\": [\"Name\", \"ID\", \"State\"], \"where\": {\"Name\": \"" + sprint + "\"} }";
        return getOidFromQuery(query);
    }

    private Oid getOidFromQuery(String query) {
        try {
            String result = iServices.executePassThroughQuery(query);
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            com.sios.stc.coseng.integration.versionone.Oid[][] oid = gson.fromJson(result,
                    com.sios.stc.coseng.integration.versionone.Oid[][].class);
            return iServices.getOid(oid[0][0].getId());
        } catch (Exception e) {
            throw new RuntimeException("Unable to perform query [" + query + "]", e);
        }
    }

    private void setFields(IAssetType assetType, Asset asset, List<Field> fields) {
        /*
         * Set backlog field attributes in addition to provided config. Non empty 'name'
         * suggests that value will be 'NAME:VALUE'. Empty 'name' will have value
         * 'VALUE'
         */
        try {
            for (Field field : fields) {
                String attribute = field.getAttribute();
                String name = field.getName();
                String value = field.getValue();
                if (name != null && !name.isEmpty())
                    value = name + Separator.ATTR.get() + value;
                log.debug("Attr [{}], name [{}], value [{}]", attribute, name, value);
                IAttributeDefinition attr = assetType.getAttributeDefinition(attribute);
                asset.setAttributeValue(attr, value);
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to set attribute value", e);
        }
    }

    private Map<String, String> getTestNgParameters() {
        Map<String, String> params = new HashMap<String, String>();
        try {
            params.putAll(
                    test.getTestNg().getContext().getIInvokedMethod().getTestMethod().getXmlTest().getAllParameters());
        } catch (Exception ignore) {
            // do nothing
        }
        return params;
    }

    private String getTestInputParameters() {
        List<String> testParams = new ArrayList<String>();
        try {
            Map<String, String> params = getTestNgParameters();
            for (String name : params.keySet()) {
                String value = params.get(name);
                testParams.add(
                        new Stringer(name).htmlBold() + Separator.PARAM_VALUE.get() + new Stringer(value).htmlItalic());
            }
        } catch (Exception ignore) {
            // do nothing
        }
        return StringUtils.join(testParams, Stringer.Html.LINE_BREAK.get());
    }

    private List<Field> getTestParamFields(TriggerOn trigger, TestPhase phase) {
        List<Field> fields = new ArrayList<Field>();
        try {
            Map<String, String> testParams = getTestNgParameters();
            for (String paramName : testParams.keySet()) {
                String paramValue = testParams.get(paramName);
                List<Field> matches = v1.getTest().getParamFields(paramName, paramValue, trigger, phase);
                if (matches != null)
                    fields.addAll(matches);
            }
        } catch (Exception ignore) {
            /* Not all TestNG execution phases may have test parameters. */
        }
        return fields;
    }

    private boolean hasRequiredFields() {
        try {
            Field field = v1.getBacklog().getField(V1Attr.STATUS.get(), TriggerOn.TESTNGEXECUTION, TestPhase.START);
            if (!hasValidField(field))
                return false;
            field = v1.getTest().getField(V1Attr.STATUS.get(), TriggerOn.TESTNGMETHOD, TestPhase.START);
            if (!hasValidField(field))
                return false;
            field = v1.getTest().getField(V1Attr.STATUS.get(), TriggerOn.TESTNGMETHOD, TestPhase.PASS);
            if (!hasValidField(field))
                return false;
            field = v1.getTest().getField(V1Attr.STATUS.get(), TriggerOn.TESTNGMETHOD, TestPhase.FAIL);
            if (!hasValidField(field))
                return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean hasValidField(Field field) {
        if (field == null || field.getAttribute() == null || field.getAttribute().isEmpty() || field.getName() == null
                || field.getName().isEmpty() || field.getValue() == null || field.getValue().isEmpty())
            return false;
        return true;
    }

}
