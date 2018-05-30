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
package com.sios.stc.coseng.aut;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.jsoup.Jsoup;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.SkipException;

import com.sios.stc.coseng.integration.IIntegratorReport;
import com.sios.stc.coseng.integration.Integrator;
import com.sios.stc.coseng.run.Test;
import com.sios.stc.coseng.util.Stringer;

public final class Log implements IIntegratorReport {

    public LogAssertions.LogAssert     hardAssert = null;
    public LogAssertions.LogAssert     ha         = null;
    public LogAssertions.LogSoftAssert softAssert = null;
    public LogAssertions.LogSoftAssert sa         = null;

    public enum Prefix {
        ASSERTION("+ "), ASSERTION_SOFT("~ "), MESSAGE("# "), TEST_STEP("> "), TEST_STEP_SKIP("- "), TASK("| ");

        private String prefix;

        private Prefix(String prefix) {
            this.prefix = prefix;
        }

        public String get() {
            return prefix;
        }
    }

    private Test                                         test          = null;
    private LogAssertions                                logAssertions = null;
    private static final org.apache.logging.log4j.Logger log           = LogManager.getLogger(Log.class);

    Log(Test test, Window window) {
        this.test = test;
        logAssertions = new LogAssertions(test, this, window);
        hardAssert = logAssertions.new LogAssert();
        ha = hardAssert;
        softAssert = logAssertions.new LogSoftAssert();
        sa = softAssert;
    }

    public void results(org.apache.logging.log4j.Level logLevel, String logMessage, String expectedResult,
            String actualResult) {
        String logHeader = getLogHeader();
        if (logLevel == null)
            logLevel = Level.INFO;
        boolean resultsMatch = true;
        if (expectedResult != null && !expectedResult.equals(actualResult))
            resultsMatch = false;
        Stringer stringer = new Stringer(logLevel);
        String logEntry = StringUtils.EMPTY;
        if (org.apache.logging.log4j.Level.INFO.equals(logLevel))
            logEntry = stringer.wrapParentheses().toString();
        else if (org.apache.logging.log4j.Level.WARN.equals(logLevel))
            logEntry = stringer.wrapParentheses().htmlBold().toString();
        else if (org.apache.logging.log4j.Level.ERROR.equals(logLevel))
            logEntry = stringer.wrapParentheses().htmlBold().htmlItalic().toString();
        else if (org.apache.logging.log4j.Level.DEBUG.equals(logLevel))
            logEntry = stringer.wrapParentheses().toString();
        else if (org.apache.logging.log4j.Level.FATAL.equals(logLevel))
            logEntry = stringer.wrapParentheses().htmlBold().htmlItalic().toString();
        else if (org.apache.logging.log4j.Level.TRACE.equals(logLevel))
            logEntry = stringer.wrapParentheses().toString();
        /*
         * Logger will record success/failure by logLevel and doesn't need the appended
         * results.
         */
        String result = StringUtils.EMPTY;
        if (actualResult != null) {
            if (!resultsMatch)
                actualResult = new Stringer(actualResult).htmlBold().htmlItalic().toString();
            result = "; result " + Stringer.wrapBracket(actualResult);
        }
        if (log.isTraceEnabled())
            log.log(logLevel, "{} {}{}", logHeader, Jsoup.parse(logMessage).text(), Jsoup.parse(result).text());
        /* Send to TestNG Reporter log */
        Reporter.log(Stringer.htmlLineBreak(logEntry + logMessage + result));
        /* Notify integrators */
        notifyIntegrators(logLevel, logMessage, expectedResult, actualResult);
    }

    public String testStep(String step) {
        String logMessage = Prefix.TEST_STEP.get()
                + new Stringer("Step " + Stringer.wrapBracket(step)).htmlBold().toString();
        results(null, logMessage, null, null);
        return Jsoup.parse(logMessage).text();
    }

    public String message(String message) {
        return message(message, null);
    }

    public String message(String message, org.apache.logging.log4j.Level logLevel) {
        String logMessage = Prefix.MESSAGE.get() + "Message " + Stringer.wrapBracket(message);
        results(logLevel, logMessage, null, null);
        return Jsoup.parse(logMessage).text();
    }

    public void skipTestForBrowser() {
        skipTestForBrowser(null);
    }

    public void skipTestForBrowser(String message) {
        skipTestForBrowser(message, org.apache.logging.log4j.Level.WARN);
    }

    public void skipTestForBrowser(String message, org.apache.logging.log4j.Level logLevel) {
        String logMessage = Stringer.htmlItalic(Prefix.TEST_STEP_SKIP.get() + "Skip test for browser "
                + Stringer.wrapBracket(test.getSelenium().getBrowser().getType()));
        if (message != null)
            logMessage += Stringer.htmlItalic(", message " + Stringer.wrapBracket(message));
        results(logLevel, logMessage, null, null);
        test.getTestNg().getContext().getIInvokedMethod().getTestResult().setStatus(ITestResult.SKIP);
        throw new SkipException(Jsoup.parse(logMessage).text());
    }

    public void skipTestForDefect(String defectId) {
        skipTestForDefect(defectId, null);
    }

    public void skipTestForDefect(String defectId, String message) {
        skipTestForDefect(defectId, message, org.apache.logging.log4j.Level.WARN);
    }

    public void skipTestForDefect(String defectId, String message, org.apache.logging.log4j.Level logLevel) {
        String logMessage = Stringer.htmlItalic(Prefix.TEST_STEP_SKIP.get() + "Skip test for defect "
                + Stringer.wrapBracket(defectId) + ", message " + Stringer.wrapBracket(message));
        results(logLevel, logMessage, null, null);
        test.getTestNg().getContext().getIInvokedMethod().getTestResult().setStatus(ITestResult.SKIP);
        throw new SkipException(Jsoup.parse(logMessage).text());

    }

    @Override
    public void notifyIntegrators(org.apache.logging.log4j.Level logLevel, String logMessage, String expectedResult,
            String actualResult) {
        for (Integrator i : test.getTestNg().getIntegrators()) {
            i.addTestStep(logMessage);
            i.addTestStepExpectedResult(logMessage, expectedResult);
            i.addTestStepActualResult(logLevel.toString() + logMessage, actualResult);
        }
    }

    private String getLogHeader() {
        String logHeader = StringUtils.EMPTY;
        List<String> headers = new ArrayList<String>();
        try {
            headers.add("cosengTest [" + test.getId());
            headers.add("suite [" + test.getTestNg().getContext().getISuite().getName());
            headers.add("test [" + test.getTestNg().getContext().getITestContext().getName());
            headers.add("class [" + test.getTestNg().getContext().getITestClass().getName());
            headers.add("method [" + test.getTestNg().getContext().getIInvokedMethod().getTestMethod().getMethodName());
        } catch (Exception e) {
            // do nothing; let through what header was able to collect
        } finally {
            logHeader = StringUtils.join(headers, "]" + Stringer.Separator.LIST.get()) + "]:";
        }
        return logHeader;
    }

}
