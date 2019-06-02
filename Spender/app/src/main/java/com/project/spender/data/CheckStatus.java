package com.project.spender.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.project.spender.fns.api.data.CheckJsonWithStatus;
import com.project.spender.fns.api.data.Status;

public class CheckStatus {

    private String time;
    private String status;

    public CheckStatus(String time) {
        this.time = time;
    }


    public synchronized void settStatus(String status) {
        this.status = status;
    }

    public synchronized String getStatus() {
        return status;
    }

    public String getTime() {
        return time;
    }
}
