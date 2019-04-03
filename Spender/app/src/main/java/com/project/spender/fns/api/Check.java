package com.project.spender.fns.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Check {

    @SerializedName("document")
    @Expose
    private Document document;

    public Receipt getData() {
        return document.receipt;
    }

}