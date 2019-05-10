package com.project.spender.fns.api.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Property {

    @SerializedName("value")
    @Expose
    public String value;

    @SerializedName("key")
    @Expose
    public String key;

}