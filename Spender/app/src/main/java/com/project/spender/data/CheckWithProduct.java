package com.project.spender.data;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class CheckWithProduct {

    @Embedded
    public Check check;

    @Relation(parentColumn = "id", entityColumn = "check_id")
    public List<Product> products;
}
