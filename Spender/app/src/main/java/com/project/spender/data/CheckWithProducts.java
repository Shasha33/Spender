package com.project.spender.data;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class CheckWithProducts {

    @Embedded
    private Check check;

    @Relation(parentColumn = "id", entityColumn = "check_id")
    private List<Product> products;

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

    public void updateCheckId (long newId) {
        check.setId(newId);
        for (Product product : products) {
            product.setCheckId(newId);
        }
    }
}
