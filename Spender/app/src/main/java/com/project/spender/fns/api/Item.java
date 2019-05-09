package com.project.spender.fns.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("sum")
    @Expose
    public Long sum;

    @SerializedName("price")
    @Expose
    public Long price;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("quantity")
    @Expose
    public Double quantity;

    @SerializedName("nds10")
    @Expose
    public Integer nds10;

    public Long check_id;

    public Item(String name, Long sum, Long price, Double quantity) {
        this(name, sum, price, quantity, (long) -1);
    }

    public Item(String name, Long sum, Long price, Double quantity, Long check_id) {
        this.sum = sum;
        this.price = price;
        this.name = name;
        this.quantity = quantity;
        this.check_id = check_id;
    }

    @Override
    public String toString() {
        return name + " " + sum + " " + price + " " + quantity + " " + check_id;
    }
}