package com.project.spender.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.project.spender.fns.api.data.Json.Item;

import java.util.Objects;

import static android.arch.persistence.room.ForeignKey.CASCADE;


/**
 * Класс описывающий товар и его представление в бд.
 */
@Entity(foreignKeys =
            @ForeignKey(onDelete = CASCADE, entity = Check.class, parentColumns = "id", childColumns = "check_id"),
        indices = {@Index("check_id")})
public class Product implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;

    private long sum;

    private long price;

    private double quantity;

    @ColumnInfo(name = "check_id")
    private long checkId;

    @Ignore
    public Product(String name, long sum, long price, double quantity, long checkId) {
        this(0, name, sum, price, quantity, checkId);
    }

    public Product(long id, String name, long sum, long price, double quantity, long checkId) {
        this.id = id;
        this.name = name;
        this.sum = sum;
        this.price = price;
        this.quantity = quantity;
        this.checkId = checkId;
    }

    public Product(Item item) {
        this(item.name, item.sum, item.price, item.quantity, 0);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return sum == product.sum &&
                price == product.price &&
                Double.compare(product.quantity, quantity) == 0 &&
                Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, sum, price, quantity);
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

    public long getSum() {
        return sum;
    }

    public void setSum(long sum) {
        this.sum = sum;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public long getCheckId() {
        return checkId;
    }

    public void setCheckId(long checkId) {
        this.checkId = checkId;
    }

    @Override
    public String toString() {
        return name + " " + sum + " " + price +  " " + quantity + " " + checkId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLongArray(new long[]{id, sum, price, checkId});
        dest.writeString(name);
        dest.writeDouble(quantity);
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {

        @Override
        public Product createFromParcel(Parcel source) {
            long[] longs = new long[4];
            source.readLongArray(longs);
            return new Product(longs[0], source.readString(),
                    longs[1], longs[2], source.readDouble(), longs[3]);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
