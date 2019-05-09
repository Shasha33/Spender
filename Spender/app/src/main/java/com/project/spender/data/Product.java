package com.project.spender.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;


@Entity(foreignKeys = @ForeignKey(entity =
        Check.class, parentColumns = "id", childColumns = "check_id"))
public class Product {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;

    public long sum;

    public long price;

    public double quantity;

    @ColumnInfo(name = "check_id")
    public long checkId;

    @Override
    public String toString() {
        return name + " " + sum + " " + price +  " " + quantity + " " + checkId;
    }
}
