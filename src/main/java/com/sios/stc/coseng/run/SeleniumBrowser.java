package com.sios.stc.coseng.run;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.google.gson.annotations.Expose;
import com.sios.stc.coseng.exceptions.CosengConfigException;
import com.sios.stc.coseng.run.SeleniumWebDriver.Location;
import com.sios.stc.coseng.util.Stringer;

public final class SeleniumBrowser {

    @Expose
    private Browser               type       = null;
    @Expose
    private SeleniumBrowserOption option     = null;
    @Expose
    private File                  executable = null;

    public enum Browser {
        FIREFOX, CHROME, EDGE, IE, SAFARI, OPERA;
    }

    public Browser getType() {
        return type;
    }

    public SeleniumBrowserOption getOption() {
        return option;
    }

    public File getExecutable() {
        return executable;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    void validateAndPrepare() {
        try {
            if (type == null)
                throw new IllegalArgumentException("Field browser must be provided; supported browsers "
                        + Stringer.wrapBracket(StringUtils.join(Browser.values(), Stringer.Separator.LIST.get())));

            if (option == null)
                option = new SeleniumBrowserOption();
            option.validateAndPrepare(type);
        } catch (Exception e) {
            throw new CosengConfigException(e);
        }
    }

    void validateExecutable(Location location) {
        try {
            if (executable != null && Location.NODE.equals(location) && !executable.canExecute())
                throw new IllegalArgumentException("Browser " + Stringer.wrapBracket(type) + " executable "
                        + Stringer.wrapBracket(executable) + " must exist and be executable");
        } catch (Exception e) {
            throw new CosengConfigException(e);
        }
    }

}
