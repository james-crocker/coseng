package com.sios.stc.coseng.run;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.google.gson.annotations.Expose;

public final class CosengUri {

    @Expose
    private Boolean           enableFind = null;
    @Expose
    private CosengUriValidate validate   = null;

    private UriFound uriFound = null;

    public boolean isEnableFind() {
        return enableFind;
    }

    public CosengUriValidate getValidate() {
        return validate;
    }

    public UriFound getUriFound() {
        return uriFound;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toStringExclude(this, "uriFound");
    }

    void validateAndPrepare(Directory directory, TestNgContext context) {
        if (enableFind == null)
            enableFind = false;
        if (validate == null)
            validate = new CosengUriValidate();
        validate.validateAndPrepare();
        uriFound = new UriFound(directory, context, validate);
    }

}
