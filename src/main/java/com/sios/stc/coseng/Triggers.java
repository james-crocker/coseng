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
package com.sios.stc.coseng;

import org.testng.xml.XmlSuite.ParallelMode;

public final class Triggers {

    public enum TestPhase {
        START, FINISH, PASS, FAIL, SKIPPED;
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
