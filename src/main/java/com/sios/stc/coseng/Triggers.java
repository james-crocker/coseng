package com.sios.stc.coseng;

import org.testng.xml.XmlSuite.ParallelMode;

public final class Triggers {

    public enum Phase {
        START, FINISH, PASS, FAIL;
    }

    public enum TriggerOn {
        COSENG, TESTNGEXECUTION, TESTNGSUITE, TESTNGTEST, TESTNGCLASS, TESTNGMETHOD;
    }

    @SuppressWarnings("deprecation")
    public static boolean isTrigger(ParallelMode mode, TriggerOn trigger) {
        /*
         * While parallel mode FALSE is deprecated in the Java, the TestNG DTD v1.1
         * still supports it.
         */
        if (TriggerOn.TESTNGTEST.equals(trigger) && (ParallelMode.TESTS.equals(mode) || ParallelMode.FALSE.equals(mode)
                || ParallelMode.NONE.equals(mode)))
            return true;
        if (TriggerOn.TESTNGCLASS.equals(trigger)
                && (ParallelMode.CLASSES.equals(mode) || ParallelMode.INSTANCES.equals(mode)))
            return true;
        if (TriggerOn.TESTNGMETHOD.equals(trigger) && ParallelMode.METHODS.equals(mode))
            return true;
        return false;
    }

}
