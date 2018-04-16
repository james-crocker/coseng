package com.sios.stc.coseng.run;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.google.gson.annotations.Expose;

public final class CosengScreenshot {

    @Expose
    private Boolean enable             = null;
    @Expose
    private Boolean enableOnAssertFail = null;

    public boolean isEnable() {
        return enable;
    }

    public boolean isEnableOnAssertFail() {
        return enableOnAssertFail;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    void validateAndPrepare() {
        if (enable == null)
            enable = false;
        if (enableOnAssertFail == null)
            enableOnAssertFail = false;
    }

}
