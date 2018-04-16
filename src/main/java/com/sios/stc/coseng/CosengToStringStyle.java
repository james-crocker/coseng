package com.sios.stc.coseng;

import org.apache.commons.lang3.builder.ToStringStyle;

public class CosengToStringStyle extends ToStringStyle {

    private static final long serialVersionUID = -5152733810006290498L;

    public static final ToStringStyle DEFAULT         = new Default();
    public static final ToStringStyle WITH_CLASS_NAME = new WithClassName();

    private static final class Default extends ToStringStyle {

        private static final long serialVersionUID = -9071858448571659634L;

        Default() {
            super();
            this.setUseClassName(false);
            this.setUseShortClassName(true);
            this.setUseIdentityHashCode(false);
            this.setFieldSeparator(", ");
            this.setFieldNameValueSeparator("=");
            this.setFieldSeparatorAtStart(false);
            this.setFieldSeparatorAtEnd(false);
        }

    }

    private static final class WithClassName extends ToStringStyle {

        private static final long serialVersionUID = 1721841433986223125L;

        WithClassName() {
            super();
            this.setUseClassName(true);
            this.setUseShortClassName(true);
            this.setUseIdentityHashCode(false);
            this.setFieldSeparator(", ");
            this.setFieldNameValueSeparator("=");
            this.setFieldSeparatorAtStart(false);
            this.setFieldSeparatorAtEnd(false);
        }

    }

}
