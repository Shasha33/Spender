package com.project.spender.fns.api.exception;

import retrofit2.Response;

/**
 * Исключение кидаемое NetworkManager.
 * Может быть пораждено неправильным http кодом, либо другим исключением.
 */
public class NetworkException extends Exception {
    private Response response;

    public int getCode() {
        return (response != null)? response.code() : -1;
    }

    public String cleverGetMessage() {
        return (response != null)? response.message() : "-1";
    }

    public NetworkException(String message, Response response) {
        super(message);
        this.response = response;
    }

    public NetworkException(Throwable cause) {
        super(cause);
    }
}
