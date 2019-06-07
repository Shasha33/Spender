package com.project.spender.fns.api.data;

public enum Status {
    SUCCESS, //check received
    EXIST, //check exists, but not received yet
    WRONG_RESPONSE_ERROR, //unexpected response code
    NETWORK_ERROR, // cannot connect with server
    SENDING //request for existing
}
