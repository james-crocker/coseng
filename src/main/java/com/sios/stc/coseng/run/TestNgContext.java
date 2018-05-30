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
package com.sios.stc.coseng.run;

import org.testng.IInvokedMethod;
import org.testng.ISuite;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.xml.XmlSuite.ParallelMode;

public final class TestNgContext {

    private static final InheritableThreadLocal<ISuite>         iSuite         = new InheritableThreadLocal<ISuite>();
    private static final InheritableThreadLocal<ITestContext>   iTestContext   = new InheritableThreadLocal<ITestContext>();
    private static final InheritableThreadLocal<ITestClass>     iTestClass     = new InheritableThreadLocal<ITestClass>();
    private static final InheritableThreadLocal<IInvokedMethod> iInvokedMethod = new InheritableThreadLocal<IInvokedMethod>();
    private static final InheritableThreadLocal<ParallelMode>   parallelMode   = new InheritableThreadLocal<ParallelMode>();
    private static final InheritableThreadLocal<Boolean>        useWebDriver   = new InheritableThreadLocal<Boolean>();

    TestNgContext() {
        useWebDriver.set(true);
    }

    public ISuite getISuite() {
        return iSuite.get();
    }

    void setISuite(ISuite iSuite) {
        /* allow set to null */
        TestNgContext.iSuite.set(iSuite);
    }

    public boolean isUseWebDriver() {
        return useWebDriver.get();
    }

    void setUseWebDriver(String use) {
        if (use == null || use.isEmpty())
            useWebDriver.set(true);
        else
            useWebDriver.set(Boolean.getBoolean(use));
    }

    public ParallelMode getParallelMode() {
        return parallelMode.get();
    }

    void setParallelMode(ParallelMode parallelMode) {
        /* allow set to null */
        TestNgContext.parallelMode.set(parallelMode);
    }

    public ITestContext getITestContext() {
        return iTestContext.get();
    }

    void setITestContext(ITestContext iTestContext) {
        /* allow set to null */
        TestNgContext.iTestContext.set(iTestContext);
    }

    public ITestClass getITestClass() {
        return iTestClass.get();
    }

    void setITestClass(ITestClass iTestClass) {
        /* allow set to null */
        TestNgContext.iTestClass.set(iTestClass);
    }

    public IInvokedMethod getIInvokedMethod() {
        return iInvokedMethod.get();
    }

    void setIInvokedMethod(IInvokedMethod iInvokedMethod) {
        /* allow set to null */
        TestNgContext.iInvokedMethod.set(iInvokedMethod);
    }

}
