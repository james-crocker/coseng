package com.sios.stc.coseng.run;

import org.testng.asserts.Assertion;
import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;

import com.sios.stc.coseng.Common;

public class Assertions {

    protected static final String SUCCESS = "success";
    protected static final String FAIL    = "fail";

    private static final String SCREENSHOT_ASSERTION_FAIL_PREFIX = "assertionFailure";

    private static int assertFailureCount = 0;

    /**
     * The Class LogAssert.
     *
     * @since 3.0
     * @version.coseng
     */
    public class LogAssert extends Assertion {
        /*
         * getExpected and getActual return less than helpful values depending
         * on the assert method. ie. assertNotNull, assertEquals. So, rather
         * than doing a laundry list of reflections just report on success or on
         * failure.
         */

        /*
         * (non-Javadoc)
         * 
         * @see org.testng.asserts.Assertion#onAssertSuccess(org.testng.asserts.
         * IAssert)
         */
        @Override
        public synchronized void onAssertSuccess(IAssert<?> a) {
            Logging.logResults(CosengRunner.getTest(), org.apache.logging.log4j.Level.INFO,
                    Logging.ASSERTION_PREFIX + Logging.getLogMessage(a), SUCCESS, SUCCESS);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.testng.asserts.Assertion#onAssertFailure(org.testng.asserts.
         * IAssert, java.lang.AssertionError)
         */
        @Override
        public synchronized void onAssertFailure(IAssert<?> a, AssertionError ex) {
            Logging.logResults(CosengRunner.getTest(), org.apache.logging.log4j.Level.ERROR,
                    Logging.ASSERTION_PREFIX + Logging.getLogMessage(a), SUCCESS, FAIL);
            if (CosengRunner.getTest().isAllowScreenshotOnAssertFailure()) {
                CosengRunner.saveScreenshot(SCREENSHOT_ASSERTION_FAIL_PREFIX
                        + Common.FILENAME_SEPARATOR + assertFailureCount++);
            }
        }
    }

    /**
     * The Class LogSoftAssert.
     *
     * @since 3.0
     * @version.coseng
     */
    public class LogSoftAssert extends SoftAssert {
        /*
         * (non-Javadoc)
         * 
         * @see org.testng.asserts.Assertion#onAssertSuccess(org.testng.asserts.
         * IAssert)
         */
        @Override
        public synchronized void onAssertSuccess(IAssert<?> a) {
            Logging.logResults(CosengRunner.getTest(), org.apache.logging.log4j.Level.INFO,
                    Logging.ASSERTION_SOFT_PREFIX + Logging.getLogMessage(a), SUCCESS, SUCCESS);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.testng.asserts.Assertion#onAssertFailure(org.testng.asserts.
         * IAssert, java.lang.AssertionError)
         */
        @Override
        public synchronized void onAssertFailure(IAssert<?> a, AssertionError ex) {
            Logging.logResults(CosengRunner.getTest(), org.apache.logging.log4j.Level.WARN,
                    Logging.ASSERTION_SOFT_PREFIX + Logging.getLogMessage(a), SUCCESS, FAIL);
            if (CosengRunner.getTest().isAllowScreenshotOnAssertFailure()) {
                CosengRunner.saveScreenshot(SCREENSHOT_ASSERTION_FAIL_PREFIX
                        + Common.FILENAME_SEPARATOR + assertFailureCount++);
            }
        }
    }

}
