package com.project.spender.data.entities;

import androidx.room.Embedded;

import java.util.List;

public class TagWithSumAndDate {
    @Embedded
    public Tag tag;
    public long sum;
    public String date;
}
