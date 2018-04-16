package com.sios.stc.coseng.run;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public final class ExceptionThreadFactory implements ThreadFactory {

    private Thread.UncaughtExceptionHandler handler;

    protected ExceptionThreadFactory() {
        this.handler = new ExceptionThreadHandler();
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = Executors.defaultThreadFactory().newThread(runnable);
        thread.setUncaughtExceptionHandler(handler);
        return thread;
    }

}
