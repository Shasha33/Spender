package com.project.spender;

import android.util.Log;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.util.ArrayUtils;
import com.project.spender.data.entities.Check;
import com.project.spender.data.entities.CheckWithProducts;
import com.project.spender.data.entities.Product;
import com.project.spender.data.entities.ProductTagJoin;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class CheckListHolder {

    @NonNull private String substring;
    @NonNull private String begin;
    @NonNull private String end;
    private List<CheckWithProducts> list;

    public CheckListHolder() {
        list = ChecksRoller.getInstance().getAppDatabase().getCheckDao().getAll();
    }

    /**
     * Converts date from DD.MM.YYYY to YYYY-MM-DD
     * terrible code (todo) fix it
     */
    private static String dateConvert(String date) throws IllegalArgumentException{
        String[] args = date.split(".");
        if (args.length != 3) {
            throw new IllegalArgumentException();
        }

        //:(
        String tmp = args[0];
        args[0] =  args[2];
        args[2] = tmp;

        if (!args[0].matches("[0-9]{2}") || !args[1].matches("[0-9]{2}")
                || !args[2].matches("[0-9]{4}")) {
            throw new IllegalArgumentException();
        }

        return args[0] + "-" + args[1] + "-" + args[2];
    }

    public void setBegin(@Nullable String begin) {
        if (begin == null) {
            this.begin = "0000-00-00";
        } else {
            this.begin = dateConvert(begin);
        }
        updateState();
    }

    public void setEnd(@Nullable String end) {
        if (end == null) {
            this.end = "9999-99-99";
        } else {
            this.end = dateConvert(end);
        }
        updateState();
    }

    public void setSubstring(@Nullable String substring) {
        if (substring == null) {
            this.substring = "%";
        } else {
            this.substring = substring;
        }
        updateState();
    }

    private void updateState() {
        list = ChecksRoller.getInstance().findCheckByTimePeriodAndSubstrig(begin, end, substring);
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
