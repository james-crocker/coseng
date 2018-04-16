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
package com.sios.stc.coseng.integration;

import java.net.URI;

import com.sios.stc.coseng.Triggers.Phase;
import com.sios.stc.coseng.Triggers.TriggerOn;
import com.sios.stc.coseng.run.Test;

/**
 * The Class Integrator.
 *
 * @since 3.0
 * @version.coseng
 */
public interface IIntegrator {

    abstract public void validateAndPrepare(Test test, URI configResource);

    abstract public void actOn(TriggerOn trigger, Phase phase);

    abstract public void addTestStep(String stepMessage);

    abstract public void addTestStepExpectedResult(String stepMessage, String expetedResult);

    abstract public void addTestStepActualResult(String stepMessage, String actualResult);

}
