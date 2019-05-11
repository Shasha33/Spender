package com.project.spender.fns.api.data;

import com.project.spender.fns.api.data.Json.CheckJson;
import com.project.spender.fns.api.exception.NetworkException;

/**
 * Класс для отображения статуса запроса и возврашения результата или исключения.
 */
public class CheckJsonWithStatus {

    private CheckJson checkJson;
    private Status status;
    private NetworkException exception;

    public CheckJsonWithStatus(CheckJson checkJson, Status status, NetworkException exception) {
        this.checkJson = checkJson;
        this.status = status;
        this.exception = exception;
    }

    public CheckJson getCheckJson() {
        return checkJson;
    }

    public Status getStatus() {
        return status;
    }

    public NetworkException getException() {
        return exception;
    }
}