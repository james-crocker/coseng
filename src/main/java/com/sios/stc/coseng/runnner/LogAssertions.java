package com.sios.stc.coseng.runnner;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.StringUtils;
import org.testng.asserts.Assertion;
import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;

import com.sios.stc.coseng.run.Test;
import com.sios.stc.coseng.util.Stringer;

public final class LogAssertions {

    private enum Status {
        SUCCESS("success"), FAIL("fail");

        private String value = null;

        private Status(String value) {
            this.value = value;
        }

        public String get() {
            return value;
        }
    }

    private static final String SCREENSHOT_ASSERTION_FAIL_PREFIX = "assertionFailure";

    private static int assertFailureCount = 0;

    private enum Key {
        COSENG_MESSAGE, COSENG_EXPECTED, COSENG_ACTUAL;

        @Override
        public String toString() {
            return name();
        }
    }

    /* Map the TestNG Assertion method names to the log messages */
    private static final Map<String, Map<Boolean, String>> logMessages = new HashMap<String, Map<Boolean, String>>();

    private Test   test   = null;
    private Log    log    = null;
    private Window window = null;

    LogAssertions(Test test, Log log, Window window) {
        this.test = test;
        this.log = log;
        this.window = window;

        /* Build up catalog of message types */
        String azzert = "Assert ";
        String actRes = azzert + "actual result ";
        String eqExp = " equals expected result ";
        String whAct = " where actual result ";
        String clause = StringUtils.EMPTY;

        Map<Boolean, String> messages = new HashMap<Boolean, String>();
        messages.put(false,
                actRes + Stringer.wrapBracket(Key.COSENG_ACTUAL) + eqExp + Stringer.wrapBracket(Key.COSENG_EXPECTED));
        messages.put(true, azzert + Stringer.wrapBracket(Key.COSENG_MESSAGE) + whAct
                + Stringer.wrapBracket(Key.COSENG_ACTUAL) + eqExp + Stringer.wrapBracket(Key.COSENG_EXPECTED));
        logMessages.put("assertEquals", messages);

        messages = new HashMap<Boolean, String>();
        clause = " equals (no order) expected result ";
        messages.put(false,
                actRes + Stringer.wrapBracket(Key.COSENG_ACTUAL) + clause + Stringer.wrapBracket(Key.COSENG_EXPECTED));
        messages.put(true, azzert + Stringer.wrapBracket(Key.COSENG_MESSAGE) + whAct
                + Stringer.wrapBracket(Key.COSENG_ACTUAL) + clause + Stringer.wrapBracket(Key.COSENG_EXPECTED));
        logMessages.put("assertEqualsNoOrder", messages);

        messages = new HashMap<Boolean, String>();
        clause = "is false";
        messages.put(false, actRes + clause);
        messages.put(true, azzert + Stringer.wrapBracket(Key.COSENG_MESSAGE) + whAct + clause);
        logMessages.put("assertFalse", messages);

        messages = new HashMap<Boolean, String>();
        clause = " is not equal to expected result ";
        messages.put(false,
                actRes + Stringer.wrapBracket(Key.COSENG_ACTUAL) + clause + Stringer.wrapBracket(Key.COSENG_EXPECTED));
        messages.put(true, azzert + Stringer.wrapBracket(Key.COSENG_MESSAGE) + whAct
                + Stringer.wrapBracket(Key.COSENG_ACTUAL) + clause + Stringer.wrapBracket(Key.COSENG_EXPECTED));
        logMessages.put("assertNotEquals", messages);

        messages = new HashMap<Boolean, String>();
        clause = " is not null";
        messages.put(false, actRes + Stringer.wrapBracket(Key.COSENG_ACTUAL) + clause);
        messages.put(true, azzert + Stringer.wrapBracket(Key.COSENG_MESSAGE) + whAct
                + Stringer.wrapBracket(Key.COSENG_ACTUAL) + clause);
        logMessages.put("assertNotNull", messages);

        messages = new HashMap<Boolean, String>();
        clause = " is not the same as expected result ";
        messages.put(false,
                actRes + Stringer.wrapBracket(Key.COSENG_ACTUAL) + clause + Stringer.wrapBracket(Key.COSENG_EXPECTED));
        messages.put(true, azzert + Stringer.wrapBracket(Key.COSENG_MESSAGE) + whAct
                + Stringer.wrapBracket(Key.COSENG_ACTUAL) + clause + Stringer.wrapBracket(Key.COSENG_EXPECTED));
        logMessages.put("assertNotSame", messages);

        messages = new HashMap<Boolean, String>();
        clause = " is null";
        messages.put(false, actRes + Stringer.wrapBracket(Key.COSENG_ACTUAL) + clause);
        messages.put(true, azzert + Stringer.wrapBracket(Key.COSENG_MESSAGE) + whAct
                + Stringer.wrapBracket(Key.COSENG_ACTUAL) + clause);
        logMessages.put("assertNull", messages);

        messages = new HashMap<Boolean, String>();
        String same = " is the same as expeced result ";
        messages.put(false,
                actRes + Stringer.wrapBracket(Key.COSENG_ACTUAL) + same + Stringer.wrapBracket(Key.COSENG_EXPECTED));
        messages.put(true, azzert + Stringer.wrapBracket(Key.COSENG_MESSAGE) + whAct
                + Stringer.wrapBracket(Key.COSENG_ACTUAL) + same + Stringer.wrapBracket(Key.COSENG_EXPECTED));
        logMessages.put("assertSame", messages);

        messages = new HashMap<Boolean, String>();
        String isTrue = "is true";
        messages.put(false, actRes + isTrue);
        messages.put(true, azzert + Stringer.wrapBracket(Key.COSENG_MESSAGE) + whAct + isTrue);
        logMessages.put("assertTrue", messages);
    }

    /**
     * The Class LogAssert.
     *
     * @since 3.0
     * @version.coseng
     */
    public class LogAssert extends Assertion {
        /*
         * getExpected and getActual return less than helpful values depending on the
         * assert method. ie. assertNotNull, assertEquals. So, rather than doing a
         * laundry list of reflections just report on success or on failure.
         */

        /*
         * (non-Javadoc)
         * 
         * @see org.testng.asserts.Assertion#onAssertSuccess(org.testng.asserts.
         * IAssert)
         */
        @Override
        public void onAssertSuccess(IAssert<?> a) {
            log.results(org.apache.logging.log4j.Level.INFO, Log.Prefix.ASSERTION.get() + getMessage(a),
                    Status.SUCCESS.get(), Status.SUCCESS.get());
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.testng.asserts.Assertion#onAssertFailure(org.testng.asserts.
         * IAssert, java.lang.AssertionError)
         */
        @Override
        public void onAssertFailure(IAssert<?> a, AssertionError ex) {
            log.results(org.apache.logging.log4j.Level.ERROR, Log.Prefix.ASSERTION.get() + getMessage(a),
                    Status.SUCCESS.get(), Status.FAIL.get());
            if (test.getCoseng().getScreenshot().isEnableOnAssertFail())
                window.saveScreenshot(
                        SCREENSHOT_ASSERTION_FAIL_PREFIX + Stringer.Separator.FILENAME.get() + assertFailureCount++);
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
        public void onAssertSuccess(IAssert<?> a) {
            log.results(org.apache.logging.log4j.Level.INFO, Log.Prefix.ASSERTION_SOFT.get() + getMessage(a),
                    Status.SUCCESS.get(), Status.SUCCESS.get());
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.testng.asserts.Assertion#onAssertFailure(org.testng.asserts.
         * IAssert, java.lang.AssertionError)
         */
        @Override
        public void onAssertFailure(IAssert<?> a, AssertionError ex) {
            log.results(org.apache.logging.log4j.Level.WARN, Log.Prefix.ASSERTION_SOFT.get() + getMessage(a),
                    Status.SUCCESS.get(), Status.FAIL.get());
            if (test.getCoseng().getScreenshot().isEnableOnAssertFail())
                window.saveScreenshot(
                        SCREENSHOT_ASSERTION_FAIL_PREFIX + Stringer.Separator.FILENAME.get() + assertFailureCount++);
        }
    }

    private static String getMessage(IAssert<?> a) {
        /* index 5 contains the TestNG assertion method name */
        int i = 5;
        String message = Stringer.UNKNOWN;
        if (a != null) {
            Boolean hasMessage = true;
            if (a.getMessage() == null)
                hasMessage = false;
            StackTraceElement trace = Thread.currentThread().getStackTrace()[i];
            if (trace != null && trace.getMethodName() != null) {
                String methodName = trace.getMethodName();
                if (logMessages.containsKey(methodName) && (logMessages.get(methodName)).containsKey(hasMessage))
                    message = logMessages.get(methodName).get(hasMessage);
                /* Do string replacement */
                if (hasMessage)
                    message = message.replaceAll(Key.COSENG_MESSAGE.toString(), a.getMessage());
                message = message.replaceAll(Key.COSENG_ACTUAL.toString(), getQuotedToString(a.getActual()));
                message = message.replaceAll(Key.COSENG_EXPECTED.toString(), getQuotedToString(a.getExpected()));
            }
        }
        return message;
    }

    private static String getQuotedToString(Object obj) {
        /*
         * ReflectionToStrinBuilder seems to return {a,a},# for simple strings such as
         * "aa"; don't want that behavior.
         */
        if (obj == null)
            return Stringer.NULL;
        else if (obj instanceof String)
            return getLiteral((String) obj);
        else
            return getLiteral(
                    ReflectionToStringBuilder.toString((Object) obj, ToStringStyle.SIMPLE_STYLE, true, false));
    }

    private static String getLiteral(String string) {
        if (string == null)
            string = Stringer.NULL;
        return java.util.regex.Matcher.quoteReplacement(string);
    }

}
