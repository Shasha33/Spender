package com.project.spender.fns.api.data;

import com.project.spender.fns.api.data.Json.CheckJson;
import com.project.spender.fns.api.exception.NetworkException;

/**
 * Класс для отображения статуса запроса и возврашения результата или исключения.
 */
public class CheckJsonWithStatus extends StatusWithResponse{

    private CheckJson checkJson;

    public CheckJsonWithStatus(CheckJson checkJson, Status status, NetworkException exception) {
        super(status, exception);
        this.checkJson = checkJson;
    }

    public CheckJson getCheckJson() {
        return checkJson;
    }
}
