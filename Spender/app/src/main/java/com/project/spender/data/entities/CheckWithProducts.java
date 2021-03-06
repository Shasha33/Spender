package com.project.spender.data.entities;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import com.project.spender.fns.api.data.Json.CheckJson;
import com.project.spender.fns.api.data.Json.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс описывающий чек c товарами и его взаимоотношение с таблицами в бд.
 */
public class CheckWithProducts {

    @Embedded
    private Check check;

    @Relation(parentColumn = "id", entityColumn = "check_id")
    private List<Product> products;

    public CheckWithProducts(Check check) {
        this(check, new ArrayList<>());
    }

    @Ignore
    public CheckWithProducts(Check check, List<Product> products) {
        this.check = check;
        this.products = products;
    }

    public CheckWithProducts(CheckJson checkJson) {
        check = new Check(checkJson);
        products = new ArrayList<>();
        for (Item item : checkJson.getData().items) {
            products.add(new Product(item));
        }
    }

    public Check getCheck() {
        return check;
    }

    public void setCheck(Check check) {
        this.check = check;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    /**
     * Sets new id for check and new check id for its products
     */
    public void updateCheckId (long newId) {
        check.setId(newId);
        for (Product product : products) {
            product.setCheckId(newId);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckWithProducts that = (CheckWithProducts) o;
        return Objects.equals(check, that.check) &&
                Objects.equals(products, that.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(check, products);
    }
}
