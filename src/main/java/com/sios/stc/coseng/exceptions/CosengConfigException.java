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
package com.sios.stc.coseng.exceptions;

/**
 * The Class CosengException exists to marshal the multitude of potential
 * runtime exceptions into a single exception.
 *
 * @since 2.0
 * @version.coseng
 */
public final class CosengConfigException extends IllegalArgumentException {

    private static final long serialVersionUID = 1997753363232807009L;

    /**
     * Instantiates a new coseng exception.
     *
     * @since 2.0
     * @version.coseng
     */
    public CosengConfigException() {
    }

    /**
     * Instantiates a new coseng exception.
     *
     * @param message
     *            the message
     * @since 2.0
     * @version.coseng
     */
    public CosengConfigException(String message) {
        super(message);
    }

    /**
     * Instantiates a new coseng exception.
     *
     * @param cause
     *            the cause
     * @since 2.0
     * @version.coseng
     */
    public CosengConfigException(Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new coseng exception.
     *
     * @param message
     *            the message
     * @param cause
     *            the cause
     * @since 2.0
     * @version.coseng
     */
    public CosengConfigException(String message, Throwable cause) {
        super(message, cause);
    }

}
