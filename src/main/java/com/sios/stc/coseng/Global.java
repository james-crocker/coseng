package com.sios.stc.coseng;

import org.apache.commons.lang3.builder.ToStringBuilder;

public final class Global {

    static void set() {
        /* Set global toStringBuiler property */
        ToStringBuilder.setDefaultStyle(CosengToStringStyle.DEFAULT);
    }

}
