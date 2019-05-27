package com.project.spender.data.entities;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.project.spender.fns.api.data.Json.CheckJson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

/**
 * Класс описывающий чек и его представление в бд.
 */
@Entity
public class Check {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;

    @ColumnInfo(name = "total_sum")
    private long totalSum;

    private String shop;

//    private String date;
    private int year;

    private int month;

    private int day;

    private int hour;

    private int minute;

    private int second;

    public Check(long id, String name, long totalSum, String shop, int year, int month, int day, int hour, int minute, int second) {
        this.id = id;
        this.name = name;
        this.totalSum = totalSum;
        this.shop = shop;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    @Ignore
    public Check(String name, long totalSum, String shop, String date) {
        this(0, name, totalSum, shop, date);
    }

    /**
     * @param id уникальное число в базе данных, если 0, то генерится автоматически
     * @param name имя чека
     * @param totalSum сумма в копейках
     * @param shop информация о магазине
     * @param dateString формат вида yyyy-MM-dd\'T\'HH:mm:ss например 2018-05-17T17:57:00
     */
    @Ignore
    public Check(long id, String name, long totalSum, String shop, String dateString) {
        this.id = id;
        this.name = name;
        this.totalSum = totalSum;
        this.shop = shop;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss", Locale.ROOT);
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(format.parse(dateString));
        } catch (ParseException e) {
            Log.wtf("CHECK", "Cannot parse date from string. Exception: " + e.getMessage());
        }

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        second = calendar.get(Calendar.SECOND);
    }

    public Check(CheckJson checkJson) {
        this(checkJson.getData().user, checkJson.getData().totalSum, checkJson.getData().user, checkJson.getData().dateTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Check check = (Check) o;
        return totalSum == check.totalSum &&
                year == check.year &&
                month == check.month &&
                day == check.day &&
                hour == check.hour &&
                minute == check.minute &&
                second == check.second &&
                Objects.equals(name, check.name) &&
                Objects.equals(shop, check.shop);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, totalSum, shop, year, month, day, hour, minute, second);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(long totalSum) {
        this.totalSum = totalSum;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }
}
