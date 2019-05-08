package com.project.spender.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.project.spender.Good;
import com.project.spender.fns.api.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemsDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = ItemsDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "Items.db";
    private static final int DATABASE_VERSION = 2;

    public ItemsDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_CHECKS_TABLE = "CREATE TABLE " + CheckContract.CheckEntry.TABLE_NAME + " ("
                + CheckContract.CheckEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CheckContract.CheckEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                CheckContract.CheckEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                CheckContract.CheckEntry.COLUMN_TOTAL_SUM + " INTEGER NOT NULL, " +
                CheckContract.CheckEntry.COLUMN_SHOP + " TEXT);";

        sqLiteDatabase.execSQL(SQL_CREATE_CHECKS_TABLE);

        String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE " + CheckContract.ItemEntry.TABLE_NAME + " ("
                + CheckContract.ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CheckContract.ItemEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                CheckContract.ItemEntry.COLUMN_SUM + " INTEGER NOT NULL, " +
                CheckContract.ItemEntry.COLUMN_QUANTITY + " REAL NOT NULL, " +
                CheckContract.ItemEntry.COLUMN_PRICE + " INTEGER NOT NULL, " +
                CheckContract.ItemEntry.COLUMN_CHECK_ID + " INTEGER NOT NULL, " +
                "    FOREIGN KEY (" + CheckContract.ItemEntry.COLUMN_CHECK_ID + ") REFERENCES " +
                    CheckContract.CheckEntry.TABLE_NAME + " (" + CheckContract.CheckEntry._ID + ")" +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Удаляем старую таблицу и создаём новую
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CheckContract.CheckEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CheckContract.ItemEntry.TABLE_NAME);
        // Создаём новую таблицу
        onCreate(sqLiteDatabase);
    }

    public long insertItem(String name, long sum, double quantity, long price, long check_id) {
        // Gets the database in write mode
        SQLiteDatabase db = getWritableDatabase();
        // Создаем объект ContentValues, где имена столбцов ключи,
        // а информация о госте является значениями ключей
        ContentValues values = new ContentValues();
        values.put(CheckContract.ItemEntry.COLUMN_NAME, name);
        values.put(CheckContract.ItemEntry.COLUMN_SUM, sum);
        values.put(CheckContract.ItemEntry.COLUMN_QUANTITY, quantity);
        values.put(CheckContract.ItemEntry.COLUMN_PRICE, price);
        values.put(CheckContract.ItemEntry.COLUMN_CHECK_ID, check_id);

        return db.insert(CheckContract.ItemEntry.TABLE_NAME, null, values);
    }

    public long insertCheck(String name, double total_sum, String shop, String date) {
        // Gets the database in write mode
        SQLiteDatabase db = getWritableDatabase();
        // Создаем объект ContentValues, где имена столбцов ключи,
        // а информация о госте является значениями ключей
        ContentValues values = new ContentValues();
        values.put(CheckContract.CheckEntry.COLUMN_NAME, name);
        values.put(CheckContract.CheckEntry.COLUMN_TOTAL_SUM, total_sum);
        values.put(CheckContract.CheckEntry.COLUMN_SHOP, shop);
        values.put(CheckContract.CheckEntry.COLUMN_DATE, date);

        return db.insert(CheckContract.CheckEntry.TABLE_NAME, null, values);
    }

    public List<Item> getAllByCursor(Cursor cursor) {
        List<Item> items = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(CheckContract.ItemEntry.COLUMN_NAME));
            long sum = cursor.getLong(cursor.getColumnIndex(CheckContract.ItemEntry.COLUMN_SUM));
            long price = cursor.getLong(cursor.getColumnIndex(CheckContract.ItemEntry.COLUMN_PRICE));
            double quantity = cursor.getDouble(cursor.getColumnIndex(CheckContract.ItemEntry.COLUMN_QUANTITY));
            long check_id = cursor.getLong(cursor.getColumnIndex(CheckContract.ItemEntry.COLUMN_CHECK_ID));
            items.add(new Item(name, sum, price, quantity, check_id));
        }
        return items;
    }

    public List<Item> getAllByName(String subString) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + CheckContract.ItemEntry.TABLE_NAME + " WHERE name LIKE '%"+ subString + "%'";
        Cursor cursor = db.rawQuery(query, null);

        return getAllByCursor(cursor);
    }

    public List<Item> getAll() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + CheckContract.ItemEntry.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        return getAllByCursor(cursor);
    }

//    public List<Item> getAllByDateRange(String beginDate, String endDate) {
//        SQLiteDatabase db = getReadableDatabase();
//        String query = "SELECT * FROM " + CheckContract.ItemEntry.TABLE_NAME + " WHERE date" +
//                " BETWEEN '" + beginDate + "' AND '" + endDate + "'";
//        Cursor cursor = db.rawQuery(query, null);
//
//        return getAllByCursor(cursor);
//    }

    public void clear() {
        onUpgrade(getWritableDatabase(), 0, 0);
    }
}
