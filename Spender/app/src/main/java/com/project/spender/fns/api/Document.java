package com.project.spender.fns.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Document {

    @SerializedName("receipt")
    @Expose
    public Receipt receipt;


}