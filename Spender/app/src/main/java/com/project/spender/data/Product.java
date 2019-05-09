package com.project.spender.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;


@Entity(foreignKeys =
            @ForeignKey(entity = Check.class, parentColumns = "id", childColumns = "check_id"),
        indices = {@Index("check_id")})
public class Product {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;

    private long sum;

    private long price;

    private double quantity;

    @ColumnInfo(name = "check_id")
    private long checkId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSum() {
        return sum;
    }

    public void setSum(long sum) {
        this.sum = sum;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public long getCheckId() {
        return checkId;
    }

    public void setCheckId(long checkId) {
        this.checkId = checkId;
    }

    @Override
    public String toString() {
        return name + " " + sum + " " + price +  " " + quantity + " " + checkId;
    }
}
