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

    /**
     *  @param sum
     * @param price
     * @param name
     * @param quantity
     */
    public Item(Integer sum, Integer price, String name, Double quantity) {
        super();
        this.sum = sum;
        this.price = price;
        this.name = name;
        this.quantity = quantity;
    }

}