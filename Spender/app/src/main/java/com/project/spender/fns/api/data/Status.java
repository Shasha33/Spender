package com.project.spender.fns.api.data;

public enum Status {
    SUCCESS, //check received
    EXIST, //check exists, but not received yet
    ERROR, //error occurred
    SENDING //request for existing
}
