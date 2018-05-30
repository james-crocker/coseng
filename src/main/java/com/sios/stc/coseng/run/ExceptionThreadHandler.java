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
package com.sios.stc.coseng.run;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ExceptionThreadHandler implements Thread.UncaughtExceptionHandler {

    private static final Logger log = LogManager.getLogger(ExceptionThreadHandler.class);

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("Thread exception {}", t, e);
    }

}
