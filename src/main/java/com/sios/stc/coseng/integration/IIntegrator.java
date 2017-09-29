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

import java.io.File;

import com.sios.stc.coseng.integration.Integrator.TriggerOn;
import com.sios.stc.coseng.run.CosengException;
import com.sios.stc.coseng.run.Test;

public interface IIntegrator {

    /**
     * Notify integrators when the TestNG test states changes.
     *
     * @param trigger
     *            the trigger
     * @throws CosengException
     *             the coseng exception
     * @since 3.0
     * @version.coseng
     */
    public void notifyIntegrators(TriggerOn trigger) throws CosengException;

    /**
     * Notify integrators of the report and resource directories. Used primarily
     * to attach the reports and resources used for the tests at the conclusion
     * of the test execution.
     *
     * @param test
     *            the test
     * @param reportDirectory
     *            the report directory
     * @param resourceDirectory
     *            the resource directory
     * @throws CosengException
     *             the coseng exception
     * @since 3.0
     * @version.coseng
     */
    public void notifyIntegrators(Test test, File reportDirectory, File resourceDirectory)
            throws CosengException;

}
