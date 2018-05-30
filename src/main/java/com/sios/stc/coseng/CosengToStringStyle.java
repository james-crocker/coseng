/*
 * Concurrent Selenium TestNG (COSENG)
 * Copyright (c) 2013-2018 SIOS Technology Corp.  All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sios.stc.coseng;

import org.apache.commons.lang3.builder.ToStringStyle;

class CosengToStringStyle extends ToStringStyle {

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
