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
package com.sios.stc.coseng.integration.versionone;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.google.gson.annotations.Expose;
import com.sios.stc.coseng.Triggers.TestPhase;
import com.sios.stc.coseng.Triggers.TriggerOn;
import com.sios.stc.coseng.integration.versionone.Common.V1Attr;

/**
 * The Class Backlog.
 *
 * @since 3.0
 * @version.coseng
 */
public final class Backlog {

    @Expose
    private String      namePrefix = null;
    @Expose
    private List<Field> fields     = null;

    /* Backlog odd duck that can be updated in many triggerOn/phases. */

    public String getNamePrefix() {
        if (namePrefix != null)
            return namePrefix;
        return StringUtils.EMPTY;
    }

    public String getName() {
        return Common.getValue(fields, V1Attr.NAME.get(), TriggerOn.TESTNGEXECUTION, TestPhase.START);
    }

    public void setName(String name) {
        if (name != null) {
            /* Sets with TestNG test details. */
            Common.setOrAddField(true, fields, V1Attr.NAME.get(), name, TriggerOn.TESTNGEXECUTION, TestPhase.START);
            /* Sets/Adds web driver details. */
            Common.setOrAddField(true, fields, V1Attr.NAME.get(), name, TriggerOn.TESTNGMETHOD, TestPhase.START);
            Common.setOrAddField(true, fields, V1Attr.NAME.get(), name, TriggerOn.TESTNGMETHOD, TestPhase.FINISH);
        }
    }

    public String getDescription() {
        return Common.getValue(fields, V1Attr.DESCRIPTION.get(), TriggerOn.TESTNGEXECUTION, TestPhase.START);
    }

    public void setDescription(String description) {
        if (description != null) {
            /* Sets with COSENG test detail. */
            Common.setOrAddField(true, fields, V1Attr.DESCRIPTION.get(), description, TriggerOn.TESTNGEXECUTION,
                    TestPhase.START);
            /* Sets/Adds web driver detail. */
            Common.setOrAddField(true, fields, V1Attr.DESCRIPTION.get(), description, TriggerOn.TESTNGMETHOD,
                    TestPhase.START);
            Common.setOrAddField(true, fields, V1Attr.DESCRIPTION.get(), description, TriggerOn.TESTNGMETHOD,
                    TestPhase.FINISH);
            /* Sets/Adds TestNG test results. */
            Common.setOrAddField(true, fields, V1Attr.DESCRIPTION.get(), description, TriggerOn.COSENG,
                    TestPhase.FINISH);
        }
    }

    void setTimebox(String sprintOid) {
        fields.add(new Field(V1Attr.TIMEBOX.get(), StringUtils.EMPTY, sprintOid, TriggerOn.TESTNGEXECUTION,
                TestPhase.START));
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

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
