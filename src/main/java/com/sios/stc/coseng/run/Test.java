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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.time.StopWatch;

import com.google.gson.annotations.Expose;
import com.sios.stc.coseng.exceptions.CosengConfigException;
import com.sios.stc.coseng.util.Resource;
import com.sios.stc.coseng.util.UriUtil;

public final class Test {

    @Expose
    private String   id         = null;
    @Expose
    private Site     site       = null;
    @Expose
    private Selenium selenium   = null;
    @Expose
    private TestNg   testNg     = null;
    @Expose
    private Coseng   coseng     = null;
    @Expose
    private Set<URI> classPaths = null;

    private boolean                    failed                                = false;
    private static final AtomicBoolean hasAnyFailure                         = new AtomicBoolean();
    private static final AtomicInteger testsStarted                          = new AtomicInteger();
    private static final AtomicInteger testsSuccessful                       = new AtomicInteger();
    private static final AtomicInteger testsFailed                           = new AtomicInteger();
    private static final AtomicInteger testsSkipped                          = new AtomicInteger();
    private static final AtomicInteger testsFailedButWithinSuccessPercentage = new AtomicInteger();
    private static final AtomicInteger hardAssertSuccessTotal                = new AtomicInteger();
    private static final AtomicInteger hardAssertFailureTotal                = new AtomicInteger();
    private static final AtomicInteger softAssertSuccessTotal                = new AtomicInteger();
    private static final AtomicInteger softAssertFailureTotal                = new AtomicInteger();
    private StopWatch                  stopWatch                             = new StopWatch();

    public String getId() {
        return id;
    }

    public Site getSite() {
        return site;
    }

    public Selenium getSelenium() {
        return selenium;
    }

    public TestNg getTestNg() {
        return testNg;
    }

    public Coseng getCoseng() {
        return coseng;
    }

    public Set<URI> getClassPaths() {
        Set<URI> newSet = new HashSet<URI>();
        newSet.addAll(classPaths);
        return newSet;
    }

    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    public void setHasAnyFailure(boolean hasFailure) {
        hasAnyFailure.set(hasFailure);
    }

    public boolean getHasAnyFailure() {
        return hasAnyFailure.get();
    }

    public int getTestsStarted() {
        return testsStarted.get();
    }

    public void incrementTestsStarted() {
        testsStarted.getAndIncrement();
    }

    public int getTestsSuccessful() {
        return testsSuccessful.get();
    }

    public void incrementTestsSuccessful() {
        testsSuccessful.getAndIncrement();
    }

    public int getTestsFailed() {
        return testsFailed.get();
    }

    public void incrementTestsFailed() {
        testsFailed.getAndIncrement();
    }

    public int getTestsSkipped() {
        return testsSkipped.get();
    }

    public void incrementTestsSkipped() {
        testsSkipped.getAndIncrement();
    }

    public int getTestsFailedButWithinSuccessPercentage() {
        return testsFailedButWithinSuccessPercentage.get();
    }

    public void incrementTestsFailedButWithinSuccessPercentage() {
        testsFailedButWithinSuccessPercentage.getAndIncrement();
    }

    public int getHardAssertSuccessTotal() {
        return hardAssertSuccessTotal.get();
    }

    public int incrementHardAssertSuccessTotal() {
        return hardAssertSuccessTotal.getAndIncrement();
    }

    public int getHardAssertFailureTotal() {
        return hardAssertFailureTotal.get();
    }

    public int incrementHardAssertFailureTotal() {
        return hardAssertFailureTotal.getAndIncrement();
    }

    public int getSoftAssertSuccessTotal() {
        return softAssertSuccessTotal.get();
    }

    public int incrementSoftAssertSuccessTotal() {
        return softAssertSuccessTotal.getAndIncrement();
    }

    public int getSoftAssertFailureTotal() {
        return softAssertFailureTotal.get();
    }

    public int incrementSoftAssertFailureTotal() {
        return softAssertFailureTotal.getAndIncrement();
    }

    public StopWatch getStopWatch() {
        return stopWatch;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, "failed", "hasAnyFailure", "testsStarted",
                "testsSuccessful", "testsFailed", "testsSkipped", "testsFailedButWithinSuccessPercentage", "stopWatch");
    }

    void validateAndPrepare() {
        if (classPaths != null) {
            if (!classPaths.isEmpty()) {
                Set<URI> canonicalClassPaths = new HashSet<URI>();
                for (URI uri : classPaths) {
                    try {
                        canonicalClassPaths.add(UriUtil.getCanonical(uri));
                    } catch (URISyntaxException | IOException e) {
                        throw new CosengConfigException(e);
                    }
                }
                classPaths = canonicalClassPaths;
                Resource.addClassPathsToThread(classPaths);
            }
        }
        if (site == null)
            site = new Site();
        site.validateAndPrepare();

        if (selenium == null)
            selenium = new Selenium();
        selenium.validateAndPrepare(this);

        if (testNg == null)
            testNg = new TestNg();
        testNg.validateAndPrepare(this);

        if (coseng == null)
            coseng = new Coseng();
        coseng.validateAndPrepare(this);

        /*
         * Selenium web driver service setup depends on testNg directories being
         * constructed.
         */
        selenium.getWebDriverContext().prepareDriverService();
    }

}
