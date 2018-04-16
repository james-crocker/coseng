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

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.google.gson.annotations.Expose;
import com.sios.stc.coseng.Triggers.Phase;
import com.sios.stc.coseng.Triggers.TriggerOn;

/**
 * The Class Field.
 *
 * @since 3.0
 * @version.coseng
 */
public final class Field {

    @Expose
    private String    attribute = null;
    @Expose
    private String    name      = null;
    @Expose
    private String    value     = null;
    @Expose
    private TriggerOn triggerOn = null;
    @Expose
    private Phase     phase     = null;

    Field(String attribute, String name, String value, TriggerOn triggerOn, Phase phase) {
        this.attribute = attribute;
        this.name = name;
        this.value = value;
        this.triggerOn = triggerOn;
        this.phase = phase;
    }

    /**
     * Gets the attribute.
     *
     * @return the attribute
     * @since 3.0
     * @version.coseng
     */
    String getAttribute() {
        return attribute;
    }

    /**
     * Gets the name.
     *
     * @return the name
     * @since 3.0
     * @version.coseng
     */
    String getName() {
        return name;
    }

    /**
     * Gets the value.
     *
     * @return the value
     * @since 3.0
     * @version.coseng
     */
    String getValue() {
        return value;
    }

    void setValue(String value) {
        if (value != null) {
            this.value = value;
        }
    }

    TriggerOn getTriggerOn() {
        return triggerOn;
    }

    Phase getPhase() {
        return phase;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
