package com.project.spender.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

/**
 * Таблица развязки.
 */
@Entity(tableName = "product_tag_join",
        primaryKeys = {"product_id", "tag_id"},
        foreignKeys = {
            @ForeignKey(entity = Product.class,
                        parentColumns = "id",
                        childColumns = "product_id"),
            @ForeignKey(entity = Tag.class,
                        parentColumns = "id",
                        childColumns = "tag_id")
        })
public class ProductTagJoin {
    @ColumnInfo(name = "product_id")
    private long productId;

    @ColumnInfo(name = "tag_id")
    private long tagId;

    public ProductTagJoin(long productId, long tagId) {
        this.productId = productId;
        this.tagId = tagId;
    }

    public long getProductId() {
        return productId;
    }

    public long getTagId() {
        return tagId;
    }
}
