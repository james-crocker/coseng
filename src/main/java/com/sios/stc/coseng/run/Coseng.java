package com.sios.stc.coseng.run;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.google.gson.annotations.Expose;

public final class Coseng {

    @Expose
    private CosengScreenshot screenshot = null;
    @Expose
    private CosengUri        uri        = null;

    public CosengScreenshot getScreenshot() {
        return screenshot;
    }

    public CosengUri getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    void validateAndPrepare(Test test) {
        if (screenshot == null)
            screenshot = new CosengScreenshot();
        screenshot.validateAndPrepare();
        if (uri == null)
            uri = new CosengUri();
        uri.validateAndPrepare(test.getTestNg().getDirectory(), test.getTestNg().getContext());
    }

}
