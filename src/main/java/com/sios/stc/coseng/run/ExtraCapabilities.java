package com.sios.stc.coseng.run;

import java.util.LinkedHashMap;
import java.util.Map;

import org.openqa.selenium.Capabilities;

import com.google.gson.annotations.Expose;

public final class ExtraCapabilities implements Capabilities {

    @Expose
    private Map<String, ?> capabilities = null;

    @Override
    public Map<String, ?> asMap() {
        if (capabilities == null)
            capabilities = new LinkedHashMap<String, Object>();
        return capabilities;
    }

    @Override
    public Object getCapability(String arg0) {
        if (capabilities != null)
            return capabilities.get(arg0);
        return null;
    }

}
