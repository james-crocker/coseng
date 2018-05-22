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
package com.sios.stc.coseng.run;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestNGListener;
import org.testng.TestNG;

import com.sios.stc.coseng.Triggers.TestPhase;
import com.sios.stc.coseng.Triggers.TriggerOn;
import com.sios.stc.coseng.integration.Integrator;
import com.sios.stc.coseng.util.Stringer;

/**
 * The Class Concurrent creates a runnable instance for a given COSENG test.
 *
 * @since 2.0
 * @version.coseng
 */
public final class RunnableTest implements Runnable {

    private static final Logger log = LogManager.getLogger(RunnableTest.class);
    private Test                test;

    RunnableTest(Test test) {
        if (test == null) {
            throw new IllegalArgumentException("Test must be provided");
        }
        this.test = test;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        TestNG testNg = new TestNG();
        String testId = test.getId();
        Thread thread = Thread.currentThread();
        thread.setName(testId);
        log.info("Executing test {}", test);
        log.debug("RunnableTest [{}], threadId [{}], thread [{}], threadParent [{}]", testId, thread.getId(), thread,
                thread.getThreadGroup().getParent().getName());
        try {
            /*
             * Assure creation of the CosengListener and registration of the test in this
             * *child* thread as test is static and distinct from other threads class loader
             * for CosengListener!
             */
            testNg.addListener((ITestNGListener) new TestNgListener(test));
            testNg.setXmlSuites(test.getTestNg().getXmlSuites());
            testNg.setVerbose(test.getTestNg().getVerbosity());
            testNg.setOutputDirectory(test.getTestNg().getDirectory().getReports().getPath());
            test.getStopWatch().start();
            testNg.run();
            test.setFailed(testNg.hasFailure());
        } catch (Exception e) {
            test.setFailed(true);
            throw new RuntimeException("Unable to complete TestNG execution for test " + Stringer.wrapBracket(testId),
                    e);
        } finally {
            if (test.getStopWatch().isStarted()) {
                test.getStopWatch().stop();
            }
            int startedWebDriver = test.getSelenium().getWebDriverContext().getStartedWebDrivers();
            int stoppedWebDriver = test.getSelenium().getWebDriverContext().getStoppedWebDrivers();
            if (startedWebDriver != stoppedWebDriver) {
                test.setFailed(true);
            }
            Level logLevel = Level.INFO;
            if (startedWebDriver != stoppedWebDriver) {
                logLevel = Level.ERROR;
                test.setFailed(true);
            }
            log.log(logLevel, "Test {} total web driver started {}, stopped {} {}", Stringer.wrapBracket(testId),
                    Stringer.wrapBracket(test.getSelenium().getWebDriverContext().getStartedWebDrivers()),
                    Stringer.wrapBracket(test.getSelenium().getWebDriverContext().getStoppedWebDrivers()),
                    (startedWebDriver != stoppedWebDriver ? "; Web driver started/stopped not equal"
                            : StringUtils.EMPTY));
            /* Notify integrators of final results */
            for (Integrator i : test.getTestNg().getIntegrators()) {
                i.actOn(TriggerOn.COSENG, TestPhase.FINISH);
            }
            log.log(Level.INFO, "Test {} elapsed time (hh:mm:ss:ms) {}", Stringer.wrapBracket(testId),
                    Stringer.wrapBracket(test.getStopWatch().toString()));
            log.log(Level.INFO,
                    "Test {} total tests started {}, successful {}, failed {}, skipped {}, failed with successful percentage {}",
                    Stringer.wrapBracket(testId), Stringer.wrapBracket(test.getTestsStarted()),
                    Stringer.wrapBracket(test.getTestsSuccessful()), Stringer.wrapBracket(test.getTestsFailed()),
                    Stringer.wrapBracket(test.getTestsSkipped()),
                    Stringer.wrapBracket(test.getTestsFailedButWithinSuccessPercentage()));
            logLevel = Level.INFO;
            if (test.isFailed())
                logLevel = Level.ERROR;
            log.log(logLevel, "Test {} completed {} ", Stringer.wrapBracket(testId),
                    (test.isFailed() ? "with failures" : "successfully"));
        }
    }

}
