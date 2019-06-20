package com.project.spender.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import static androidx.room.ForeignKey.CASCADE;

/**
 * Таблица развязки.
 */
@Entity(tableName = "product_tag_join",
        primaryKeys = {"product_id", "tag_id"},
        foreignKeys = {
            @ForeignKey(onDelete = CASCADE,
                        entity = Product.class,
                        parentColumns = "id",
                        childColumns = "product_id"),
            @ForeignKey(onDelete = CASCADE,
                        entity = Tag.class,
                        parentColumns = "id",
                        childColumns = "tag_id")
        },
        indices = {@Index("product_id"), @Index("tag_id")}
        )
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
