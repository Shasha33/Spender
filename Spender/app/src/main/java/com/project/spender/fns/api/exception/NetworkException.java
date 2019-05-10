package com.project.spender.fns.api.exception;

public class NetworkException extends Exception {
    private int code;

    public int getCode() {
        return code;
    }

    public NetworkException(String message, int code) {
        super(message);
        this.code = code;
    }
}
