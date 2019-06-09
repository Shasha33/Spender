package com.project.spender.data.entities;

import androidx.room.Embedded;

/**
 * Structure to store tag and total coast of products with it
 */
public class TagWithSum {
    @Embedded
    public Tag tag;
    public long sum;
}
