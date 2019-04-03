package com.project.spender;

import android.support.annotation.NonNull;

public class Good {
    public String name;
    public String date;
    public String shop;
    public double price;

    public Good(@NonNull String name, @NonNull  String date,
                double price, @NonNull String shop) {
        this.name = name;
        this.date = date;
        this.shop = shop;
        this.price = price;
    }

    @Override
    public String toString() {
        return name + " " + price + " " + date +  " " + shop + "\n";
    }
}
