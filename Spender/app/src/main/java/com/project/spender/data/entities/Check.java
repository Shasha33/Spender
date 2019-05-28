package com.project.spender.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.project.spender.fns.api.data.Json.CheckJson;

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

    private String date;

    @Ignore
    public Check(String name, long totalSum, String shop, String date) {
        this(0, name, totalSum, shop, date);
    }

    /**
     * @param id уникальное число в базе данных, если 0, то генерится автоматически
     * @param name имя чека
     * @param totalSum сумма в копейках
     * @param shop информация о магазине
     * @param date формат вида yyyy-MM-ddTHH:mm:ss например 2018-05-17T17:57:00
     */
    public Check(long id, String name, long totalSum, String shop, String date) {
        this.id = id;
        this.name = name;
        this.totalSum = totalSum;
        this.shop = shop;
        this.date = date;
    }

    @Ignore
    public Check(CheckJson checkJson) {
        this(checkJson.getData().user, checkJson.getData().totalSum, checkJson.getData().retailPlaceAddress, checkJson.getData().dateTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Check check = (Check) o;
        return totalSum == check.totalSum &&
                Objects.equals(name, check.name) &&
                Objects.equals(shop, check.shop) &&
                Objects.equals(date, check.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, totalSum, shop, date);
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}