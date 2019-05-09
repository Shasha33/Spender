package com.project.spender.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

@Dao
public abstract class CheckDao {

    @Query("SELECT * FROM Product")
    public abstract List<Product> getAllProducts();

    @Query("SELECT * FROM Product")
    public abstract List<Product> getAllChecks();

    @Transaction
    @Query("SELECT * FROM `Check`")
    public abstract List<CheckWithProducts> getAll();

    @Insert
    public abstract void insertCheck(Check check);

    @Insert
    public abstract void insertProduct(Product product);

    @Transaction
    public void insertCheckWithProducts(CheckWithProducts checkWithProducts) {
        insertCheck(checkWithProducts.getCheck());
        checkWithProducts.updateCheckId(checkWithProducts.getCheck().getId());
        for (Product product : checkWithProducts.getProducts()) {
            insertProduct(product);
        }
    }
}
