package com.project.spender.data.entities;

import java.util.List;

public class ProductWithTags {

    private Product product;
    private List<Tag> tags;

    public ProductWithTags(Product product, List<Tag> tags) {
        this.product = product;
        this.tags = tags;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
