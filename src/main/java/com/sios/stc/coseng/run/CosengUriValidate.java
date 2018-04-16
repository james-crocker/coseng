package com.sios.stc.coseng.run;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.google.gson.annotations.Expose;
import com.sios.stc.coseng.exceptions.CosengConfigException;

public final class CosengUriValidate {

    @Expose
    private Boolean        enable                    = null;
    @Expose
    private Set<String>    skipHtmlTags              = null;
    @Expose
    private Set<URI>       skipUris                  = null;
    @Expose
    private Boolean        failTestOnInvalid         = null;
    @Expose
    private HttpMethod     requestMethod             = null;
    @Expose
    private Integer        requestTimeoutMillisecond = null;
    @Expose
    private BrowserVersion browserVersion            = null;
    @Expose
    private Boolean        useInsecureSsl            = null;
    @Expose
    private Boolean        enableJavaScript          = null;
    @Expose
    private Boolean        enableCss                 = null;
    @Expose
    private Boolean        enableDownloadImages      = null;

    public boolean isEnabled() {
        return enable;
    }

    public Set<String> getSkipHtmlTags() {
        Set<String> newSet = new HashSet<String>();
        if (skipHtmlTags != null)
            newSet.addAll(skipHtmlTags);
        return newSet;
    }

    public Set<URI> getSkipUris() {
        Set<URI> newSet = new HashSet<URI>();
        if (skipUris != null)
            newSet.addAll(skipUris);
        return newSet;
    }

    public boolean isFailTestOnInvalid() {
        return failTestOnInvalid;
    }

    public HttpMethod getRequestMethod() {
        return requestMethod;
    }

    public int getRequestTimeoutMillisecond() {
        return requestTimeoutMillisecond;
    }

    public BrowserVersion getBrowserVersion() {
        return browserVersion;
    }

    public boolean isUseInsecureSsl() {
        return useInsecureSsl;
    }

    public boolean isEnableJavaScript() {
        return enableJavaScript;
    }

    public boolean isEnableCss() {
        return enableCss;
    }

    public boolean isEnableDownloadImages() {
        return enableDownloadImages;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    void validateAndPrepare() {
        try {
            if (enable == null)
                enable = false;
            if (getSkipHtmlTags().contains(null))
                throw new IllegalArgumentException("Field skipHtmlTags must not contain null elements");
            if (getSkipUris().contains(null))
                throw new IllegalArgumentException("Field skipUris must not contain null elements");
            if (failTestOnInvalid == null)
                failTestOnInvalid = false;
            if (requestMethod == null)
                requestMethod = HttpMethod.HEAD;
            if (requestTimeoutMillisecond == null)
                requestTimeoutMillisecond = new Integer(3000);
            if (browserVersion == null)
                browserVersion = BrowserVersion.BEST_SUPPORTED;
            if (useInsecureSsl == null)
                useInsecureSsl = false;
            if (enableJavaScript == null)
                enableJavaScript = false;
            if (enableCss == null)
                enableCss = false;
            if (enableDownloadImages == null)
                enableDownloadImages = false;
        } catch (Exception e) {
            throw new CosengConfigException(e);
        }
    }

}
