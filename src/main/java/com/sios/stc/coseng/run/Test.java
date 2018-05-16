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

    private boolean       failed                                = false;
    private AtomicBoolean hasAnyFailure                         = new AtomicBoolean();
    private AtomicInteger testsStarted                          = new AtomicInteger();
    private AtomicInteger testsSuccessful                       = new AtomicInteger();
    private AtomicInteger testsFailed                           = new AtomicInteger();
    private AtomicInteger testsSkipped                          = new AtomicInteger();
    private AtomicInteger testsFailedButWithinSuccessPercentage = new AtomicInteger();
    private StopWatch     stopWatch                             = new StopWatch();

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
