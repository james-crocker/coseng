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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sios.stc.coseng.ExitStatus;
import com.sios.stc.coseng.util.Stringer;

/**
 * The Class CosengTests contains the entry method for executing Concurrent
 * Selenium TestNG (COSENG) suites.
 *
 * @since 2.0
 * @version.coseng
 */
public class CosengTests {

    private static final Logger log = LogManager.getLogger(CosengTests.class);

    /**
     * The method for executing Concurrent Selenium TestNG (COSENG) suites. Requires
     * a Tests JSON resource and an optional Node JSON resource. If all tests are
     * successful the exit value is [0]. On failure the exit value is [1]. The
     * concurrent executor timeout is [5] minutes.
     *
     * @param args
     *            the command line arguments to configure a COSENG test execution;
     *            -help for usage and options
     * @see com.sios.stc.coseng.RunTests#main(String[])
     * @see com.sios.stc.coseng.run.BrowserNode.webdriver.node.SeleniumNode
     * @see com.sios.stc.coseng.config.TestConfig
     * @see com.sios.stc.coseng.tests.TestsConfig
     * @since 2.0
     * @version.coseng
     */
    protected static void run(Tests tests) {
        log.info("COSENG initializing");
        tests.validateAndPrepare();
        /*
         * Create the concurrent workers for each test; execute tests in parallel and
         * shutdown
         */
        boolean executionFailure = false;
        ExecutorService executorPool = Executors.newCachedThreadPool(new ExceptionThreadFactory());
        StopWatch stopWatch = new StopWatch();
        List<Runnable> runnableTests = new ArrayList<Runnable>();
        for (Test test : tests) {
            try {
                runnableTests.add(new RunnableTest(test));
            } catch (Exception e) {
                log.fatal("Unable to create runnable test " + Stringer.wrapBracket(test.getId()), e);
                System.exit(ExitStatus.FAILURE.getStatus());
            }
        }

        stopWatch.start();
        try {
            for (Runnable test : runnableTests) {
                executorPool.execute(test);
            }
            executorPool.shutdown();
            executorPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch (RejectedExecutionException e) {
            executionFailure = true;
            log.fatal("Unable to execute test", e);
        } catch (InterruptedException e) {
            executionFailure = true;
            log.fatal("Test execution timeout exceeded", e);
        } finally {
            stopWatch.stop();
        }

        List<String> failedTests = tests.getFailed();
        ExitStatus systemExitCode = ExitStatus.SUCCESS;
        if (executionFailure || !failedTests.isEmpty()) {
            log.error("Testing completed; with failures " + (!failedTests.isEmpty() ? failedTests : ""));
            systemExitCode = ExitStatus.FAILURE;
        } else {
            log.info("Testing completed; all tests completed successfully");
        }
        log.info("Total elapsed time (hh:mm:ss:ms) {}", Stringer.wrapBracket(stopWatch.toString()));
        /* Report the test results */
        log.info("Reports @ " + tests.getOutputDirectories());
        log.info("COSENG terminated");
        System.exit(systemExitCode.getStatus());
    }

}
