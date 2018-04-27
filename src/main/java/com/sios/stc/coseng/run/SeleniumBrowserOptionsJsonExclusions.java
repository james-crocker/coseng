package com.sios.stc.coseng.run;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class SeleniumBrowserOptionsJsonExclusions implements ExclusionStrategy {

    public boolean shouldSkipClass(Class<?> clazz) {
        /*-
         * org.openqa.selenium.firefox.FirefoxOptions faults on class:
         *   org.apache.commons.exec.ExecuteException, field "cause"
         */
        if (clazz.equals(org.apache.commons.exec.ExecuteException.class)) {
            return true;
        }
        return false;
    }

    public boolean shouldSkipField(FieldAttributes arg0) {
        /*-
         * org.openqa.selenium.firefox.FirefoxOptions faults on private class:
         *   org.openqa.selenium.os.OsProcess$SeleniumWatchDog; skip by field
         */
        if (arg0.getName().equals("process")) {
            return true;
        }
        return false;
    }

}
