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
