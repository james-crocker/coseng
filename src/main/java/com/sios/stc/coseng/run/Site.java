package com.sios.stc.coseng.run;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.google.gson.annotations.Expose;
import com.sios.stc.coseng.exceptions.CosengConfigException;

public final class Site {

    @Expose
    private Boolean angularApp  = null;
    @Expose
    private Boolean angular2App = null;

    public boolean isAngularApp() {
        return angularApp;
    }

    public boolean isAngular2App() {
        return angular2App;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    void validateAndPrepare() {
        try {
            /* angularApps */
            if (angularApp == null)
                angularApp = false;
            if (angular2App == null)
                angular2App = false;
            if (Boolean.TRUE.equals(angularApp) && Boolean.TRUE.equals(angular2App))
                throw new IllegalArgumentException("Either field angularApp or angular2App may be true");
        } catch (Exception e) {
            throw new CosengConfigException(e);
        }
    }

}
