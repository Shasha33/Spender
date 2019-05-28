package com.project.spender;

import android.util.Log;

import androidx.annotation.Nullable;

import com.project.spender.data.entities.CheckWithProducts;
import com.project.spender.data.entities.Product;
import com.project.spender.data.entities.ProductTagJoin;

import java.util.List;

public class CheckListHolder {

    @Nullable private String substring;
    @Nullable private String begin;
    @Nullable private String end;
    private List<CheckWithProducts> list;

    public CheckListHolder() {
        list = ChecksRoller.getInstance().getAppDatabase().getCheckDao().getAll();
    }

    public void setBegin(@Nullable String begin) {
        this.begin = begin;
    }

    public void setEnd(@Nullable String end) {
        this.end = end;
    }

    public void setSubstring(@Nullable String substring) {
        list = ChecksRoller.getInstance().findCheckByRegEx(substring);
        this.substring = substring;
    }

    public void addTag(int position, long tagId) {
        for (Product product : list.get(position).getProducts()) {
            ChecksRoller.getInstance().getAppDatabase()
                        .getCheckDao().insertExistingTagForProduct(tagId, product.getId());
        }
    }

    public void removeTag(int position, long tagId) {
        for (Product product : list.get(position).getProducts()) {
            ChecksRoller.getInstance().getAppDatabase()
                        .getCheckDao().deleteTagProductRelation(new ProductTagJoin(product.getId(), tagId));
        }
    }


    public List<CheckWithProducts> getList() {
        return list;
    }
}
