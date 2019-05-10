package com.project.spender.data;

import android.arch.persistence.room.Room;
import android.content.Context;

public class DatabaseHolder {
    private static volatile AppDatabase database;
    private static String DATABASE_NAME = "database";


    public static synchronized AppDatabase getDatabase(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context,
                    AppDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
        }
        return database;
    }

    public String getDatabaseName() {
        return DATABASE_NAME;
    }

}
