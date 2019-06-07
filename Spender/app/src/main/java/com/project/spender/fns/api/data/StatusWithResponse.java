package com.project.spender.fns.api.data;

import com.project.spender.fns.api.data.Json.CheckJson;
import com.project.spender.fns.api.exception.NetworkException;

public class StatusWithResponse {
    private Status status;
    private NetworkException exception;

    public StatusWithResponse(Status status, NetworkException exception) {
        this.status = status;
        this.exception = exception;
    }

    public Status getStatus() {
        return status;
    }

    public NetworkException getException() {
        return exception;
    }
}
