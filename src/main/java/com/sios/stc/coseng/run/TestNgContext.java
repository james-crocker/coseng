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
