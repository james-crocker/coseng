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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Reporter;
import org.testng.asserts.IAssert;

import com.sios.stc.coseng.Common;
import com.sios.stc.coseng.RunTests;
import com.sios.stc.coseng.integration.Integrator;

/**
 * The Class Logging.
 *
 * @since 3.0
 * @version.coseng
 */
public class Logging {

    protected static final String ASSERTION_PREFIX      = "+ ";
    protected static final String ASSERTION_SOFT_PREFIX = "~ ";
    protected static final String MESSAGE_PREFIX        = "# ";
    protected static final String TEST_STEP_PREFIX      = "> ";
    protected static final String TEST_STEP_SKIP_PREFIX = "- ";
    protected static final String TASK_PREFIX           = "| ";

    private static final String INFO                = "(info ) ";
    private static final String WARN                = "(warn ) ";
    private static final String ERROR               = "(error) ";
    private static final String DEBUG               = "(debug) ";
    private static final String FATAL               = "(fatal) ";
    private static final String TRACE               = "(trace) ";
    private static final String REPORTER_LINE_BREAK = "<br>";
    private static final String COSENG_EXPECTED     = "COSENG_EXPECTED";
    private static final String COSENG_ACTUAL       = "COSENG_ACTUAL";
    private static final String COSENG_MESSAGE      = "COSENG_MESSAGE";

    private static final org.apache.logging.log4j.Level DEFAULT_LOG_LEVEL =
            org.apache.logging.log4j.Level.INFO;

    private static final Logger log = LogManager.getLogger(RunTests.class.getName());

    /**
     * The Enum HasAssertionMessage.
     *
     * @since 3.0
     * @version.coseng
     */
    /* Map to match TestNG assertion method name to log messaging */
    private static enum HasAssertionMessage {
        YES, NO;
    }

    /* Map the TestNG Assertion method names to the log messages */
    private static final Map<String, Map<HasAssertionMessage, String>> logMessages =
            new HashMap<String, Map<HasAssertionMessage, String>>();

    /**
     * Inits the log messages
     *
     * @since 3.0
     * @version.coseng
     */
    private static void init() {
        if (logMessages.isEmpty()) {
            Map<HasAssertionMessage, String> messages = new HashMap<HasAssertionMessage, String>();
            messages.put(HasAssertionMessage.NO, "Assert actual result [" + COSENG_ACTUAL
                    + "] equals expected result [" + COSENG_EXPECTED + "]");
            messages.put(HasAssertionMessage.YES,
                    "Assert [" + COSENG_MESSAGE + "] where actual result [" + COSENG_ACTUAL
                            + "] equals expected result [" + COSENG_EXPECTED + "]");
            logMessages.put("assertEquals", messages);

            messages = new HashMap<HasAssertionMessage, String>();
            messages.put(HasAssertionMessage.NO, "Assert actual result [" + COSENG_ACTUAL
                    + "] equals (no order) expected result [" + COSENG_EXPECTED + "]");
            messages.put(HasAssertionMessage.YES,
                    "Assert [" + COSENG_MESSAGE + "] where actual result [" + COSENG_ACTUAL
                            + "] equals (no order) expected result [" + COSENG_EXPECTED + "]");
            logMessages.put("assertEqualsNoOrder", messages);

            messages = new HashMap<HasAssertionMessage, String>();
            messages.put(HasAssertionMessage.NO, "Assert actual result is false");
            messages.put(HasAssertionMessage.YES,
                    "Assert [" + COSENG_MESSAGE + "] where actual result is false");
            logMessages.put("assertFalse", messages);

            messages = new HashMap<HasAssertionMessage, String>();
            messages.put(HasAssertionMessage.NO, "Assert actual result [" + COSENG_ACTUAL
                    + "] is not equal to expected result [" + COSENG_EXPECTED + "]");
            messages.put(HasAssertionMessage.YES,
                    "Assert [" + COSENG_MESSAGE + "] where actual result [" + COSENG_ACTUAL
                            + "] is not equal to expected result [" + COSENG_EXPECTED + "]");
            logMessages.put("assertNotEquals", messages);

            messages = new HashMap<HasAssertionMessage, String>();
            messages.put(HasAssertionMessage.NO,
                    "Assert actual result [" + COSENG_ACTUAL + "] is not null");
            messages.put(HasAssertionMessage.YES, "Assert [" + COSENG_MESSAGE
                    + "] where actual result [" + COSENG_ACTUAL + "] is not null");
            logMessages.put("assertNotNull", messages);

            messages = new HashMap<HasAssertionMessage, String>();
            messages.put(HasAssertionMessage.NO, "Assert actual result [" + COSENG_ACTUAL
                    + "] is not the same as expected result [" + COSENG_EXPECTED + "]");
            messages.put(HasAssertionMessage.YES,
                    "Assert [" + COSENG_MESSAGE + "] where actual result [" + COSENG_ACTUAL
                            + "] is not the same as expected result [" + COSENG_EXPECTED + "]");
            logMessages.put("assertNotSame", messages);

            messages = new HashMap<HasAssertionMessage, String>();
            messages.put(HasAssertionMessage.NO,
                    "Assert actual result [" + COSENG_ACTUAL + "] is null");
            messages.put(HasAssertionMessage.YES, "Assert [" + COSENG_MESSAGE
                    + "] where actual result [" + COSENG_ACTUAL + "] is null");
            logMessages.put("assertNull", messages);

            messages = new HashMap<HasAssertionMessage, String>();
            messages.put(HasAssertionMessage.NO, "Assert actual result [" + COSENG_ACTUAL
                    + "] is the same as expected result [" + COSENG_EXPECTED + "]");
            messages.put(HasAssertionMessage.YES,
                    "Assert [" + COSENG_MESSAGE + "] where actual result [" + COSENG_ACTUAL
                            + "] is the same as expected result [" + COSENG_EXPECTED + "]");
            logMessages.put("assertSame", messages);

            messages = new HashMap<HasAssertionMessage, String>();
            messages.put(HasAssertionMessage.NO, "Assert actual result is true");
            messages.put(HasAssertionMessage.YES,
                    "Assert [" + COSENG_MESSAGE + "] where actual result is true");
            logMessages.put("assertTrue", messages);
        }
    }

    /**
     * Log skip test for defect.
     *
     * @param defectId
     *            the defect id
     * @param message
     *            the message
     * @param lineNumber
     *            the line number
     * @param logLevel
     *            the log level
     * @since 3.0
     * @version.coseng
     */
    protected static void logSkipTestForDefect(Test test, String defectId, String message,
            int lineNumber, org.apache.logging.log4j.Level logLevel) {
        init();
        String logMessage = TEST_STEP_SKIP_PREFIX + "Skip test near line number [" + lineNumber
                + "], defect [" + defectId + "], message [" + message + "]";
        logResults(test, logLevel, logMessage, null, null);
    }

    /**
     * Log skip test for browser.
     *
     * @param browser
     *            the browser
     * @param message
     *            the message
     * @param lineNumber
     *            the line number
     * @param logLevel
     *            the log level
     * @since 3.0
     * @version.coseng
     */
    protected static void logSkipTestForBrowser(Test test, String message, int lineNumber,
            org.apache.logging.log4j.Level logLevel) {
        init();
        String logMessage = TEST_STEP_SKIP_PREFIX + "Skip test near line number [" + lineNumber
                + "], browser [" + test.getBrowser() + "]";
        if (message != null) {
            logMessage += ", message [" + message + "]";
        }
        logResults(test, logLevel, logMessage, null, null);
    }

    /**
     * Log results.
     *
     * @param logLevel
     *            the log level
     * @param logMessage
     *            the log message
     * @param expectedResult
     *            the expected result
     * @param actualResult
     *            the actual result
     * @since 3.0
     * @version.coseng
     */
    protected static void logResults(Test test, org.apache.logging.log4j.Level logLevel,
            String logMessage, String expectedResult, String actualResult) {
        init();
        String logHeader = getLogHeader(test);
        String logLevelString = INFO;
        if (logLevel == null) {
            logLevel = DEFAULT_LOG_LEVEL;
        }
        if (org.apache.logging.log4j.Level.INFO.equals(logLevel)) {
            logLevelString = INFO;
        } else if (org.apache.logging.log4j.Level.WARN.equals(logLevel)) {
            logLevelString = WARN;
        } else if (org.apache.logging.log4j.Level.ERROR.equals(logLevel)) {
            logLevelString = ERROR;
        } else if (org.apache.logging.log4j.Level.DEBUG.equals(logLevel)) {
            logLevelString = DEBUG;
        } else if (org.apache.logging.log4j.Level.FATAL.equals(logLevel)) {
            logLevelString = FATAL;
        } else if (org.apache.logging.log4j.Level.TRACE.equals(logLevel)) {
            logLevelString = TRACE;
        }
        /*
         * Logger will record success/failure by logLevel and doesn't need the
         * appended results.
         */
        String result = StringUtils.EMPTY;
        if (actualResult != null) {
            result = "; result [" + actualResult + "]";
        }
        log.log(logLevel, "{} {}{}", logHeader, logMessage, result);
        /* Send to TestNG Reporter log */
        Reporter.log(logLevelString + logMessage + result + REPORTER_LINE_BREAK);
        for (Integrator i : Integrators.getWired()) {
            try {
                i.addTestStep(test, logMessage);
                i.addTestStepExpectedResult(test, logMessage, expectedResult);
                i.addTestStepActualResult(test, logLevelString + logMessage, actualResult);
            } catch (CosengException e) {
                log.error(
                        "{} Unable to add integrator [{}],test step result message [{}], expected [{}], actual [{}]: {}",
                        logHeader, i.getClass().getName(), logMessage, expectedResult, actualResult,
                        e.getMessage());
            }
        }
    }

    /**
     * Gets the log message.
     *
     * @param a
     *            the a
     * @return the log message
     * @since 3.0
     * @version.coseng
     */
    protected static String getLogMessage(IAssert<?> a) {
        init();
        /* index 4 contains the TestNG assertion method name */
        int i = 4;
        String methodName = Common.STRING_UNKNOWN;
        String message = Common.STRING_UNKNOWN;
        if (a != null) {
            HasAssertionMessage hasMessage = HasAssertionMessage.NO;
            if (a.getMessage() != null) {
                hasMessage = HasAssertionMessage.YES;
            }
            if (Thread.currentThread().getStackTrace()[i] != null
                    && Thread.currentThread().getStackTrace()[i].getMethodName() != null) {
                methodName = Thread.currentThread().getStackTrace()[i].getMethodName();
            }
            if (logMessages.containsKey(methodName)
                    && (logMessages.get(methodName)).containsKey(hasMessage)) {
                message = logMessages.get(methodName).get(hasMessage);
            }
            /* Do string replacement */
            if (a.getMessage() != null) {
                message = message.replaceAll(COSENG_MESSAGE, a.getMessage());
            }
            message = message.replaceAll(COSENG_ACTUAL, getQuotedToString(a.getActual()));
            message = message.replaceAll(COSENG_EXPECTED, getQuotedToString(a.getExpected()));
        }
        return message;
    }

    /**
     * Gets the quoted to string.
     *
     * @param obj
     *            the obj
     * @return the quoted to string
     * @since 3.0
     * @version.coseng
     */
    private static String getQuotedToString(Object obj) {
        /*
         * ReflectionToStrinBuilder seems to return {a,a},# for simple strings
         * such as "aa"; don't want that behavior.
         */
        if (obj == null) {
            return Common.STRING_NULL;
        } else if (obj instanceof String) {
            return getQuotedString((String) obj);
        } else {
            return getQuotedString(ReflectionToStringBuilder.toString((Object) obj,
                    ToStringStyle.SIMPLE_STYLE, true, false));
        }
    }

    /**
     * Gets the quoted string.
     *
     * @param string
     *            the string
     * @return the quoted string
     * @since 3.0
     * @version.coseng
     */
    private static String getQuotedString(String string) {
        if (string == null) {
            string = Common.STRING_NULL;
        }
        return java.util.regex.Matcher.quoteReplacement(string);
    }

    /**
     * Gets the log header as a combination of the TestNG test, suite, test,
     * class and method names to prepend logging.
     *
     * @return the log header
     * @since 3.0
     * @version.coseng
     */
    private static String getLogHeader(Test test) {
        String logHeader = StringUtils.EMPTY;
        if (test != null) {
            List<String> headers = new ArrayList<String>();
            try {
                headers.add("cosengTest [" + test.getName());
                headers.add("suite [" + test.getTestNgSuite().getName());
                headers.add("test [" + test.getTestNgTest().getName());
                headers.add("class [" + test.getTestNgClass().getName());
                headers.add("method [" + test.getTestNgMethod().getTestMethod().getMethodName());
            } catch (Exception e) {
                // do nothing; let through what header was able to collect
            } finally {
                logHeader = StringUtils.join(headers, "]" + Common.DEFAULT_LIST_SEPARATOR) + "]:";
            }
        }
        return logHeader;
    }

}
