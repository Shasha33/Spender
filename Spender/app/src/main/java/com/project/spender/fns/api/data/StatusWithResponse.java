package com.project.spender.fns.api.data;

import com.project.spender.fns.api.data.Json.CheckJson;
import com.project.spender.fns.api.exception.NetworkException;

import static com.project.spender.fns.api.NetworkManager.CHECK_NOT_FOUND;
import static com.project.spender.fns.api.NetworkManager.UNCORRECTED_EMAIL;
import static com.project.spender.fns.api.NetworkManager.UNCORRECTED_PHONE;
import static com.project.spender.fns.api.NetworkManager.UNCORRECTED_PHONE_OR_PASSWORD;
import static com.project.spender.fns.api.NetworkManager.UNKNOWN_PHONE;
import static com.project.spender.fns.api.NetworkManager.USER_ALREADY_EXISTS;

/**
 * Status with possible exception
 */
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

    /**
     * Returns text representation on the status
     */
    public String getUserReadableMassage() {
        switch (status) {
            case SUCCESS:
                return "Success";
            case EXIST:
                return "Check exists";
            case SENDING:
                return "Sending";
            case NETWORK_ERROR:
                return "Network error";
            case WRONG_RESPONSE_ERROR:
                switch (exception.getCode()) {
                    case CHECK_NOT_FOUND:
                        return "Check not found";
                    case USER_ALREADY_EXISTS:
                        return "User already exists";
                    case UNCORRECTED_EMAIL :
                        return "Incorrect email address";
                    case UNCORRECTED_PHONE:
                        return "Incorrect phone number";
                    case UNKNOWN_PHONE:
                        return "Phone number not found";
                    case UNCORRECTED_PHONE_OR_PASSWORD:
                        return "Incorrect phone number or password";
                }
        }
        return "Unknown error";
    }
}
