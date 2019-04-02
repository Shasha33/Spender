package com.project.spender.fns.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class Document {

    @SerializedName("receipt")
    @Expose
    Receipt receipt;


}