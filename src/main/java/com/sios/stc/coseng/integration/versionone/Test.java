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

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.google.gson.annotations.Expose;
import com.sios.stc.coseng.Triggers.TestPhase;
import com.sios.stc.coseng.Triggers.TriggerOn;

/**
 * The Class Test.
 *
 * @since 3.0
 * @version.coseng
 */
public final class Test {

    @Expose
    private List<Field>          fields      = null;
    @Expose
    private List<TestParamField> paramFields = null;

    List<Field> getFields() {
        List<Field> allFields = new ArrayList<Field>();
        if (fields != null)
            allFields.addAll(fields);
        return allFields;
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

    List<Field> getParamFields(String paramName, String paramValue, TriggerOn trigger, TestPhase phase) {
        List<Field> matchedFields = new ArrayList<Field>();
        try {
            for (TestParamField pf : paramFields) {
                if (paramName.equals(pf.getParamName()) && paramValue.equals(pf.getParamValue())) {
                    for (Field field : pf.getFields()) {
                        if (trigger.equals(field.getTriggerOn()) && phase.equals(field.getTestPhase())) {
                            matchedFields.add(field);
                        }
                    }
                }
            }
        } catch (Exception ignore) {
            // do nothing
        }
        return matchedFields;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
