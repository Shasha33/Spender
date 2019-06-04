package com.project.spender.data;

public class CheckStatus {

    private String time;
    private String status;
    private int counter;

    public CheckStatus(String time) {
        this.time = time;
        counter = 0;
    }

    public synchronized void incCounter() {
        counter++;
    }

    public synchronized int getCounter() {
        return counter;
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
