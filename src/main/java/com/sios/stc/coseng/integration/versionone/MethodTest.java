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
package com.sios.stc.coseng.integration.versionone;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.sios.stc.coseng.Triggers.TestPhase;
import com.sios.stc.coseng.Triggers.TriggerOn;
import com.sios.stc.coseng.integration.versionone.Common.Separator;
import com.sios.stc.coseng.integration.versionone.Common.V1Attr;
import com.sios.stc.coseng.util.Stringer;

public final class MethodTest {

    private com.versionone.Oid oid    = null;
    private List<Field>        fields = new ArrayList<Field>();

    com.versionone.Oid getOid() {
        return oid;
    }

    void setOid(com.versionone.Oid oid) {
        this.oid = oid;
    }

    public String getName() {
        return Common.getValue(fields, V1Attr.NAME.get(), TriggerOn.TESTNGMETHOD, TestPhase.START);
    }

    public void setName(String name) {
        if (name != null) {
            Common.setOrAddField(true, fields, V1Attr.NAME.get(), name, TriggerOn.TESTNGMETHOD, TestPhase.START);
            Common.setOrAddField(true, fields, V1Attr.NAME.get(), name, TriggerOn.TESTNGMETHOD, TestPhase.FINISH);
        }
    }

    public String getDescription() {
        return Common.getValue(fields, V1Attr.DESCRIPTION.get(), TriggerOn.TESTNGMETHOD, TestPhase.START);
    }

    public void setDescription(String description) {
        if (description != null) {
            Common.setOrAddField(true, fields, V1Attr.DESCRIPTION.get(), description, TriggerOn.TESTNGMETHOD,
                    TestPhase.START);
            Common.setOrAddField(true, fields, V1Attr.DESCRIPTION.get(), description, TriggerOn.TESTNGMETHOD,
                    TestPhase.FINISH);
        }
    }

    public String getSetup() {
        return Common.getValue(fields, V1Attr.SETUP.get(), TriggerOn.TESTNGMETHOD, TestPhase.START);
    }

    public void setSetup(String setup) {
        if (setup != null && !setup.isEmpty()) {
            Common.setOrAddField(true, fields, V1Attr.SETUP.get(), setup, TriggerOn.TESTNGMETHOD, TestPhase.START);
            Common.setOrAddField(true, fields, V1Attr.SETUP.get(), setup, TriggerOn.TESTNGMETHOD, TestPhase.FINISH);
        }
    }

    public String getInputs() {
        return Common.getValue(fields, V1Attr.INPUTS.get(), TriggerOn.TESTNGMETHOD, TestPhase.START);
    }

    public void setInputs(String inputs) {
        if (inputs != null && !inputs.isEmpty()) {
            Common.setOrAddField(true, fields, V1Attr.INPUTS.get(), inputs, TriggerOn.TESTNGMETHOD, TestPhase.START);
            Common.setOrAddField(true, fields, V1Attr.INPUTS.get(), inputs, TriggerOn.TESTNGMETHOD, TestPhase.FINISH);
        }
    }

    public void addStep(String stepMessage) {
        if (stepMessage != null) {
            String message = Stringer.htmlLineBreak(stepMessage);
            Common.setOrAddField(false, fields, V1Attr.STEPS.get(), message, TriggerOn.TESTNGMETHOD, TestPhase.FINISH);
        }
    }

    public void addStepExpectedResult(String stepMessage, String expectedResult) {
        if (stepMessage == null)
            stepMessage = Stringer.UNKNOWN;
        String message = Stringer.htmlLineBreak(stepMessage
                + (expectedResult != null ? Separator.ASSERT_RESULT.get() + expectedResult : StringUtils.EMPTY));
        Common.setOrAddField(false, fields, V1Attr.EXPECTED_RESULTS.get(), message, TriggerOn.TESTNGMETHOD,
                TestPhase.FINISH);
    }

    public void addStepActualResult(String stepMessage, String actualResult) {
        if (stepMessage == null)
            stepMessage = Stringer.UNKNOWN;
        String message = Stringer.htmlLineBreak(stepMessage
                + (actualResult != null ? Separator.ASSERT_RESULT.get() + actualResult : StringUtils.EMPTY));
        Common.setOrAddField(false, fields, V1Attr.ACTUAL_RESULTS.get(), message, TriggerOn.TESTNGMETHOD,
                TestPhase.FINISH);
    }

    void addFields(List<Field> fields) {
        if (fields != null)
            this.fields.addAll(fields);
    }

    List<Field> getFields(TriggerOn trigger, TestPhase phase) {
        return Common.getFields(fields, trigger, phase);
    }

    /**
     * Gets the field.
     *
     * @param attribute
     *            the attribute
     * @param trigger
     *            the trigger
     * @return the field
     * @since 3.0
     * @version.coseng
     */
    Field getField(String attribute, TriggerOn trigger, TestPhase phase) {
        return Common.getField(fields, attribute, trigger, phase);
    }

    String getValue(String attribute, TriggerOn trigger, TestPhase phase) {
        return Common.getValue(fields, attribute, trigger, phase);
    }

}
