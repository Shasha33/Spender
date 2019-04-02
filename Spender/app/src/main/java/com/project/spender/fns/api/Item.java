package com.project.spender.fns.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("sum")
    @Expose
    public Integer sum;

    @SerializedName("price")
    @Expose
    public Integer price;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("quantity")
    @Expose
    public Double quantity;

    @SerializedName("nds10")
    @Expose
    public Integer nds10;

}