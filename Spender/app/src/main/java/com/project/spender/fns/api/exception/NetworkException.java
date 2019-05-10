package com.project.spender.fns.api.exception;

/**
 * Исключение кидаемое NetworkManager.
 * Может быть пораждено неправильным http кодом, либо другим исключением.
 */
public class NetworkException extends Exception {
    private int code;

    public int getCode() {
        return code;
    }

    public NetworkException(String message, int code) {
        super(message);
        this.code = code;
    }

    public NetworkException(Throwable cause) {
        super(cause);
    }
}
