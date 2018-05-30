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

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IClassListener;
import org.testng.IExecutionListener;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener2;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGListener;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlSuite.ParallelMode;

import com.sios.stc.coseng.Triggers;
import com.sios.stc.coseng.Triggers.TestPhase;
import com.sios.stc.coseng.Triggers.TriggerOn;
import com.sios.stc.coseng.integration.IIntegratorListener;
import com.sios.stc.coseng.integration.Integrator;
import com.sios.stc.coseng.util.Stringer;

/**
 * The listener class for receiving all TestNG lifecycle events. This listener
 * is primarily tasked with starting and stopping a web driver at the
 * appropriate level of parallelization. The listener is registered for all
 * classes under test programmatically at construction of the TestNG test. This
 * class listens for TestNG lifecycle events and reacts to it's before and after
 * states. This listener implements all of the TestNG lifecycle events;
 * execution, suite, test, class and method. The activation of test, class and
 * method lifecycle events is influenced by the requested parallel mode. It
 * coordinates with CosengRunner to provide the appropriate web driver for the
 * given current thread. <b>Caution:</b> The listener methods must be visible
 * for access to TestNG listener contexts. Do not call these methods from within
 * the TestNG test classes or test methods. Unless you manage the test context,
 * calling these methods from within the TestNG test classes or test methods
 * will have unintended consequences.
 *
 * 
 * <dl>
 * <dt>TestNG &lt;suite parallel="false|none"&gt; and Coseng Test JSON
 * oneWebDriver "true"</dt>
 * <dd>One web driver will be started at the start of TestNG execution</dd>
 * <dd>It will persist across all suites and tests and will be the only web
 * driver presented</dd>
 * <dd>The singular web driver will be stopped at the end of TestNG
 * execution</dd>
 * <dt>TestNG &lt;suite parallel="false|none"&gt; and Coseng Test JSON
 * oneWebDriver "false"</dt>
 * <dd>A web driver will be created at the start of the suite</dd>
 * <dd>It will persist across all tests and will be the only web driver for the
 * &lt;suite&gt; under test</dd>
 * <dd>The suite's web driver will be stopped at the end of the suite</dd>
 * <dt>TestNG &lt;suite parallel="tests"&gt;</dt>
 * <dd>A separate web driver will be created for each &lt;test&gt; in the
 * suite</dd>
 * <dd>Each web driver provisioned for a suite's &lt;test&gt; will be the only
 * web driver for all &lt;classes&gt; and included &lt;method&gt;</dd>
 * <dd>Each web driver will be stopped at the conclusion of the respective
 * &lt;test&gt;</dd>
 * <dt>TestNG &lt;suite parallel="classes"&gt;</dt>
 * <dd>A separate web driver will be created for each &lt;class&gt; in the
 * suite</dd>
 * <dd>Each web driver provisioned for a suite's &lt;class&gt; will be the only
 * web driver for all included &lt;method&gt;</dd>
 * <dd>Each web driver will be stopped at the conclusion of the respective
 * &lt;class&gt;</dd>
 * <dt>TestNG &lt;suite parallel="methods"&gt;</dt>
 * <dd>A separate web driver will be created for each &lt;method&gt; in the
 * suite</dd>
 * <dd>Each web driver provisioned for a suite's &lt;method&gt; will be the only
 * web driver for the included &lt;method&gt;</dd>
 * <dd>Each web driver will be stopped at the conclusion of the respective
 * &lt;method&gt;</dd>
 * </dl>
 *
 * @since 2.0
 * @version.coseng
 */
public final class TestNgListener implements IIntegratorListener, IExecutionListener, ISuiteListener, ITestListener,
        IClassListener, IInvokedMethodListener2, IReporter, ITestNGListener {

    private static final Logger log = LogManager.getLogger(TestNgListener.class);

    private static final String            SUITE_PARAM_WEBDRIVER = "useWebDriver";
    private static final ThreadLocal<Test> staticTest            = new ThreadLocal<Test>();
    private Test                           test                  = null;
    private boolean                        isOneWebDriver        = false;

    /*-
     * General Behaviors
     * 
     * Any <suite-files> in a suite are processed *before* any <test> in that suite, regardless of the <suite-files>
     * location. ie. <test1/><suite-files1/><test2/><suite-files2/> will be executed as <suite-files suite-files1,2/>
     * then <test1/><test2/>.
     * 
     * Web driver start|stop only applicable when a <test> is accessed; then depending on 
     * parallel="none|false|tests|classes|instances|methods" will a web driver be started|stopped
     * in the thread context of the executing test, class, instances or methods.
     * 
     * parallel="methods"
     *   Executor, Suite, Test - same thread
     *   Class - new thread
     *   Method(s) each <include> method on it's own new thread
     *   *For multiple methods from the same class in <include> the first method gets class start, second does not.
     * 
     * parallel="instances"
     *   Executor, Suite, Test - same thread
     *   Class - new thread
     *   Method(s) - new thread, where all <include> methods are executed on the same thread
     * 
     * parallel="classes"
     *   Executor, Suite, Test - same thread
     *   Class(es) - new thread for each <class>
     *   Method(s) - the class <include> method members are executed in the respective class thread 
     *     
     * parallel="tests"
     *   Executor, Suite - same thread
     *   Test(s) - new thread for each <test>
     *   Class(es) & Method(s) - all <classes> and <include> methods are executed in the respective test thread
     *   
     * parallel="none|false"
     *   Executor, Suite, Test(s), Class(es) & Method(s) single threaded, sequentially.
     */

    /**
     * The Enum WebDriverAction.
     *
     * @since 2.0
     * @version.coseng
     */
    private enum WebDriverAction {
        START, STOP
    };

    TestNgListener(Test test) {
        if (test == null)
            throw new IllegalArgumentException("A test must be provided");

        log.debug("TestNgListener constructor thread [{}]", Thread.currentThread());
        isOneWebDriver = test.getSelenium().getWebDriverContext().isOneWebDriver();
        staticTest.set(test);
        this.test = test;
    }

    public static Test getTest() {
        /* !Guarantee test not null! */

        return staticTest.get();
    }

    /* <IExecutionListener> */
    /*
     * (non-Javadoc)
     * 
     * @see org.testng.IExecutionListener#onExecutionStart()
     */
    @Override
    public void onExecutionStart() {
        TriggerOn trigger = TriggerOn.TESTNGEXECUTION;
        TestPhase phase = TestPhase.START;
        /*
         * !! ALL IMPORTANT; sets up the AUT, web driver context and other contexts. !!
         */
        new com.sios.stc.coseng.aut.CosengTest();
        logDebug(trigger, phase);
        /*
         * 2018-04-03 ChromeDriver and IEDriverServer are know to be able to launch
         * single service and provide concurrent web driver. (See
         * SeleniumWebDriver.startWebDriver(), SeleniumWebDriver.startDriverService()
         * and this.onExecutionFinish())
         */
        DriverServices services = test.getSelenium().getWebDriverContext().getDriverServices();
        if (services != null && services.isConcurrentCapable()) {
            services.start();
            String serviceName = services.get().getClass().getName();
            log.debug("Started web driver service {}", Stringer.wrapBracket(serviceName));
            if (!services.isRunning())
                throw new IllegalStateException(
                        "Could not start web driver service " + Stringer.wrapBracket(serviceName));
        }
        if (isOneWebDriver)
            webDriverAction(WebDriverAction.START);
        notifyIntegrators(trigger, phase);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.testng.IExecutionListener#onExecutionFinish()
     */
    @Override
    public void onExecutionFinish() {
        TriggerOn trigger = TriggerOn.TESTNGEXECUTION;
        TestPhase phase = TestPhase.FINISH;
        logDebug(trigger, phase);
        if (test.getCoseng().getUri().isEnableFind())
            test.getCoseng().getUri().getUriFound().saveAll();
        if (isOneWebDriver)
            webDriverAction(WebDriverAction.STOP);
        DriverServices services = test.getSelenium().getWebDriverContext().getDriverServices();
        if (services != null && services.isConcurrentCapable()) {
            services.stop();
            log.debug("Stopped web driver service {}", Stringer.wrapBracket(services.get().getClass().getName()));
        }
        notifyIntegrators(trigger, phase);
        test.getSelenium().getWebDriverContext().getWebDrivers().removeWebDriverCollection();
    }
    /* </IExecutionListener> */

    /* <ISuiteListener> */
    /*
     * (non-Javadoc)
     * 
     * @see org.testng.ISuiteListener#onStart(org.testng.ISuite)
     */
    @Override
    public void onStart(ISuite arg0) {
        test.getTestNg().getContext().setUseWebDriver(arg0.getParameter(SUITE_PARAM_WEBDRIVER));
        test.getTestNg().getContext().setParallelMode(arg0.getXmlSuite().getParallel());
        test.getTestNg().getContext().setISuite(arg0);
        TriggerOn trigger = TriggerOn.TESTNGSUITE;
        TestPhase phase = TestPhase.START;
        logDebug(trigger, phase);
        notifyIntegrators(trigger, phase);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.testng.ISuiteListener#onFinish(org.testng.ISuite)
     */
    @Override
    public void onFinish(ISuite arg0) {
        /*
         * Potential TestNG bug; listener gets extra Suite START & FINISH at end of
         * suite processing where parallel is reset to none. Can ignore since *no* web
         * driver should be started/stopped on the suite trigger.
         */
        TriggerOn trigger = TriggerOn.TESTNGSUITE;
        TestPhase phase = TestPhase.FINISH;
        logDebug(trigger, phase);
        /*-
         * TODO
         * Report excluded and invoked methods?
         * ITestNGMethod m : arg0.getExcludedMethods()
         * IInvokedMethod m : arg0.getAllInvokedMethods()
         */
        notifyIntegrators(trigger, phase);
        test.getTestNg().getContext().setISuite(null);
    }
    /* </ISuiteListener> */

    /* <ITestListener> */
    /*
     * (non-Javadoc)
     * 
     * @see org.testng.ITestListener#onStart(org.testng.ITestContext)
     */
    @Override
    public void onStart(ITestContext arg0) {
        test.getTestNg().getContext().setITestContext(arg0);
        TriggerOn trigger = TriggerOn.TESTNGTEST;
        TestPhase phase = TestPhase.START;
        logDebug(trigger, phase);
        ParallelMode mode = test.getTestNg().getContext().getParallelMode();
        if (!isOneWebDriver && Triggers.isTrigger(mode, trigger))
            webDriverAction(WebDriverAction.START);
        notifyIntegrators(trigger, phase);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.testng.ITestListener#onTestFailedButWithinSuccessPercentage(org.
     * testng.ITestResult)
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
        test.incrementTestsFailedButWithinSuccessPercentage();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.testng.ITestListener#onTestFailure(org.testng.ITestResult)
     */
    @Override
    public void onTestFailure(ITestResult arg0) {
        test.incrementTestsFailed();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.testng.ITestListener#onTestSkipped(org.testng.ITestResult)
     */
    @Override
    public void onTestSkipped(ITestResult arg0) {
        test.incrementTestsSkipped();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.testng.ITestListener#onTestStart(org.testng.ITestResult)
     */
    @Override
    public void onTestStart(ITestResult arg0) {
        test.incrementTestsStarted();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.testng.ITestListener#onTestSuccess(org.testng.ITestResult)
     */
    @Override
    public void onTestSuccess(ITestResult arg0) {
        test.incrementTestsSuccessful();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.testng.ITestListener#onFinish(org.testng.ITestContext)
     */
    @Override
    public void onFinish(ITestContext arg0) {
        TriggerOn trigger = TriggerOn.TESTNGTEST;
        TestPhase phase = TestPhase.FINISH;
        logDebug(trigger, phase);
        ParallelMode mode = test.getTestNg().getContext().getParallelMode();
        if (!isOneWebDriver && Triggers.isTrigger(mode, trigger))
            webDriverAction(WebDriverAction.STOP);
        notifyIntegrators(trigger, phase);
        test.getTestNg().getContext().setITestContext(null);
    }
    /* </ITestListener> */

    /* <IClassListener> */
    /*
     * (non-Javadoc)
     * 
     * @see org.testng.IClassListener#onBeforeClass(org.testng.ITestClass)
     */
    @Override
    public void onBeforeClass(ITestClass arg0) {
        test.getTestNg().getContext().setITestClass(arg0);
        TriggerOn trigger = TriggerOn.TESTNGCLASS;
        TestPhase phase = TestPhase.START;
        logDebug(trigger, phase);
        ParallelMode mode = test.getTestNg().getContext().getParallelMode();
        if (!isOneWebDriver && Triggers.isTrigger(mode, trigger))
            webDriverAction(WebDriverAction.START);
        notifyIntegrators(trigger, phase);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.testng.IClassListener#onAfterClass(org.testng.ITestClass)
     */
    @Override
    public void onAfterClass(ITestClass arg0) {
        TriggerOn trigger = TriggerOn.TESTNGCLASS;
        TestPhase phase = TestPhase.FINISH;
        logDebug(trigger, phase);
        ParallelMode mode = test.getTestNg().getContext().getParallelMode();
        if (!isOneWebDriver && Triggers.isTrigger(mode, trigger))
            webDriverAction(WebDriverAction.STOP);
        notifyIntegrators(trigger, phase);
        test.getTestNg().getContext().setITestClass(null);
    }
    /* </IClassListener> */

    /* <IMethodListener> */
    /*
     * (non-Javadoc)
     * 
     * @see org.testng.IInvokedMethodListener#beforeInvocation(org.testng.
     * IInvokedMethod, org.testng.ITestResult)
     */
    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        beforeInvocation(method, testResult, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.testng.IInvokedMethodListener#afterInvocation(org.testng.
     * IInvokedMethod, org.testng.ITestResult)
     */
    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        afterInvocation(method, testResult, null);
    }
    /* </IMethodListener> */

    /* <IMethodListener2> */
    /*
     * (non-Javadoc)
     * 
     * @see org.testng.IInvokedMethodListener2#beforeInvocation(org.testng.
     * IInvokedMethod, org.testng.ITestResult, org.testng.ITestContext)
     */
    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
        /*
         * TODO Comment on SuiteXml with parallel=methods and tests with the same
         * classes - ONLY one class instantiation.
         * 
         * 
         * For parallel="methods" and multiple methods from the same class; first method
         * gets class start, second does not.
         */
        if (test.getTestNg().getContext().getITestClass() == null) {
            ITestClass clazz = method.getTestMethod().getTestClass();
            test.getTestNg().getContext().setITestClass(clazz);
        }
        test.getTestNg().getContext().setIInvokedMethod(method);
        if (!continueOnFailure()) {
            testResult.setStatus(ITestResult.SKIP);
            throw new SkipException("Skipping test due to prior failure and isSkipRemainingTestsOnFailure "
                    + Stringer.wrapBracket(test.getTestNg().isSkipRemainingTestsOnFailure()));
        }
        TriggerOn trigger = TriggerOn.TESTNGMETHOD;
        TestPhase phase = TestPhase.START;
        logDebug(trigger, phase);
        ParallelMode mode = test.getTestNg().getContext().getParallelMode();
        if (!isOneWebDriver && Triggers.isTrigger(mode, trigger))
            webDriverAction(WebDriverAction.START);
        notifyIntegrators(trigger, phase);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.testng.IInvokedMethodListener2#afterInvocation(org.testng.
     * IInvokedMethod, org.testng.ITestResult, org.testng.ITestContext)
     */
    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
        TriggerOn trigger = TriggerOn.TESTNGMETHOD;
        TestPhase phase = TestPhase.FINISH;
        logDebug(trigger, phase);
        if (test.getCoseng().getUri().isEnableFind())
            test.getCoseng().getUri().getUriFound().saveMethod();
        ParallelMode mode = test.getTestNg().getContext().getParallelMode();
        if (!isOneWebDriver && Triggers.isTrigger(mode, trigger))
            webDriverAction(WebDriverAction.STOP);
        notifyIntegrators(trigger, phase);
        if (!testResult.isSuccess() && testResult.getStatus() != ITestResult.SKIP)
            test.setHasAnyFailure(true);
        test.getTestNg().getContext().setIInvokedMethod(null);
    }
    /* </IMethodListener2> */

    /*
     * (non-Javadoc)
     * 
     * @see org.testng.IReporter#generateReport(java.util.List, java.util.List,
     * java.lang.String)
     */
    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        // nothing for now
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sios.stc.coseng.integration.IIntegrator#notifyIntegrators(com.sios.
     * stc.coseng.integration.Integrator.TriggerOn)
     */
    @Override
    public void notifyIntegrators(TriggerOn trigger, TestPhase phase) {
        for (Integrator i : test.getTestNg().getIntegrators()) {
            i.actOn(trigger, phase);
        }
    }

    private void webDriverAction(WebDriverAction action) {
        log.debug("Web driver action [{}], thread [{}]", action, Thread.currentThread().getId());
        if (test.getTestNg().getContext().isUseWebDriver()) {
            if (WebDriverAction.START.equals(action) && continueOnFailure())
                test.getSelenium().getWebDriverContext().startWebDriver();
            else if (WebDriverAction.STOP.equals(action))
                test.getSelenium().getWebDriverContext().stopWebDriver();
        }
    }

    private boolean continueOnFailure() {
        boolean skip = test.getTestNg().isSkipRemainingTestsOnFailure();
        if (skip && test.getHasAnyFailure())
            return false;
        return true;
    }

    private void logDebug(TriggerOn trigger, TestPhase phase) {
        TestNgContext context = test.getTestNg().getContext();
        log.debug(
                "TriggerOn [{}] phase [{}]; test [{}], thread [{}], parallelMode [{}], suite [{}], test [{}], class [{}], method [{}], startWebDrivers [{}], stopWebDrivers [{}], isOneWebDriver [{}]",
                trigger, phase, test.getId(), Thread.currentThread().getId(),
                test.getTestNg().getContext().getParallelMode(),
                (context.getISuite() != null ? context.getISuite().getName() : Stringer.NULL),
                (context.getITestContext() != null ? context.getITestContext().getName() : Stringer.NULL),
                (context.getITestClass() != null ? context.getITestClass().getName() : Stringer.NULL),
                (context.getIInvokedMethod() != null ? context.getIInvokedMethod().getTestMethod().getMethodName()
                        : Stringer.NULL),
                test.getSelenium().getWebDriverContext().getStartedWebDrivers(),
                test.getSelenium().getWebDriverContext().getStoppedWebDrivers(), isOneWebDriver);
    }

}
