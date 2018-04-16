package com.sios.stc.coseng;

public enum ExitStatus {

    SUCCESS(0), FAILURE(1);

    private int status;

    private ExitStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

}
