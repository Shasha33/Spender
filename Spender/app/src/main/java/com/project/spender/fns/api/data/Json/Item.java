package com.project.spender.fns.api.data.Json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("sum")
    @Expose
    public long sum;

    @SerializedName("price")
    @Expose
    public long price;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("quantity")
    @Expose
    public double quantity;

    @SerializedName("nds10")
    @Expose
    public long nds10;

    @Override
    public String toString() {
        return name + " " + sum + " " + price + " " + quantity;
    }
}