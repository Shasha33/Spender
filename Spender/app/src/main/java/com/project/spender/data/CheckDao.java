package com.project.spender.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

@Dao
public interface CheckDao {

    @Query("SELECT * FROM Product")
    List<Product> getAllProduct();

    @Transaction
    @Query("SELECT * FROM `Check`")
    List<CheckWithProduct> getAll();
}
