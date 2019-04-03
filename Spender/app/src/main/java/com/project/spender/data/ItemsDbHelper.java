package com.project.spender.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.project.spender.fns.api.Check;
import com.project.spender.fns.api.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemsDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = ItemsDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "Items.db";
    private static final int DATABASE_VERSION = 1;

    public ItemsDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_GUESTS_TABLE = "CREATE TABLE " + CheckContract.ItemEntry.TABLE_NAME + " ("
                + CheckContract.ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CheckContract.ItemEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                CheckContract.ItemEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                CheckContract.ItemEntry.COLUMN_PRICE + " REAL NOT NULL, " +
                CheckContract.ItemEntry.COLUMN_SHOP + " TEXT);";

        sqLiteDatabase.execSQL(SQL_CREATE_GUESTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Удаляем старую таблицу и создаём новую
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CheckContract.ItemEntry.TABLE_NAME);
        // Создаём новую таблицу
        onCreate(sqLiteDatabase);
    }

    public void insertItem(String name, String date, int price, String shop) {
        // Gets the database in write mode
        SQLiteDatabase db = getWritableDatabase();
        // Создаем объект ContentValues, где имена столбцов ключи,
        // а информация о госте является значениями ключей
        ContentValues values = new ContentValues();
        values.put(CheckContract.ItemEntry.COLUMN_NAME, name);
        values.put(CheckContract.ItemEntry.COLUMN_DATE, date);
        values.put(CheckContract.ItemEntry.COLUMN_PRICE, price);
        values.put(CheckContract.ItemEntry.COLUMN_SHOP, shop);

        long newRowId = db.insert(CheckContract.ItemEntry.TABLE_NAME, null, values);
    }

    public Cursor getAllByName(String subString) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + CheckContract.ItemEntry.TABLE_NAME + " WHERE name LIKE '%"+ subString + "%'";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;

        //        List<Item> items = new ArrayList<>();
//        while (cursor.moveToNext()) {
//            items.append(Item(cursor.getInt(cursor.getColumnIndex(CheckContract.ItemEntry.COLUMN_NAME)), ));
//        }

    }


}
