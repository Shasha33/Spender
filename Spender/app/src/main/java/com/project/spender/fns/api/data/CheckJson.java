package com.project.spender.fns.api.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckJson {

    @SerializedName("document")
    @Expose
    private Document document;

    public Receipt getData() {
        return document.receipt;
    }

}