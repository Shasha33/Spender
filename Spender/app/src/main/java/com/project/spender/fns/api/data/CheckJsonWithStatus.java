package com.project.spender.fns.api.data;

import com.project.spender.fns.api.exception.NetworkException;

public class CheckJsonWithStatus {

    private CheckJson checkJson;
    private Status currentStatus = Status.NOTHING;
    private NetworkException exception;

    public CheckJsonWithStatus(CheckJson checkJson, Status currentStatus, NetworkException exception) {
        this.checkJson = checkJson;
        this.currentStatus = currentStatus;
        this.exception = exception;
    }

}
