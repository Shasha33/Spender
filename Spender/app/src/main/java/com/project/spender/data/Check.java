package com.project.spender.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Check {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;

    @ColumnInfo(name = "total_sum")
    private long totalSum;

    private String shop;

    private String date;

    @Ignore
    public Check(String name, long totalSum, String shop, String date) {
        this(0, name, totalSum, shop, date);
    }

    public Check(long id, String name, long totalSum, String shop, String date) {
        this.id = id;
        this.name = name;
        this.totalSum = totalSum;
        this.shop = shop;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(long totalSum) {
        this.totalSum = totalSum;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
