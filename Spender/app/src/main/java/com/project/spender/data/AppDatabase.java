package com.project.spender.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.project.spender.data.entities.Check;
import com.project.spender.data.entities.Product;
@Database(entities = {Check.class, Product.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CheckDao getCheckDao();
}
