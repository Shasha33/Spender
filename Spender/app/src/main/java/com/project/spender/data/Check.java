package com.project.spender.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Check {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;

    @ColumnInfo(name = "total_sum")
    public long totalSum;

    public String shop;

    public String date;
}
