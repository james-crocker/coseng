package com.sios.stc.coseng.aut;

import com.paulhammant.ngwebdriver.NgWebDriver;
import com.sios.stc.coseng.run.Test;

public final class Angular {

    private Test test = null;

    Angular(Test test) {
        this.test = test;
    }

    public void waitToFinish() {
        /* ngWebDriver won't be available until TestNG thread starts a web driver */
        NgWebDriver ngWebDriver = test.getSelenium().getWebDriverContext().getWebDrivers().getNgWebDriver();
        /* 1.0 NgWebDriver had waitForAngular2RequestsToFinish() */
        ngWebDriver.waitForAngularRequestsToFinish();
    }

}
