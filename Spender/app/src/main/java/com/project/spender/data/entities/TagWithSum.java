package com.project.spender.data.entities;

import androidx.room.Embedded;

public class TagWithSum {
    @Embedded
    public Tag tag;
    public long sum;
}
