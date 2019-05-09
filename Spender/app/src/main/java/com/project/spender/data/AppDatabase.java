package com.project.spender.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Check.class, Product.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CheckDao checkDao();
}
