package com.project.spender;

import android.util.Log;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.project.spender.data.entities.Check;
import com.project.spender.data.entities.CheckWithProducts;
import com.project.spender.data.entities.Product;
import com.project.spender.data.entities.ProductTagJoin;

import java.util.List;

public class CheckListHolder {

    @NonNull private String regEx;
    @NonNull private String begin;
    @NonNull private String end;
    private List<CheckWithProducts> list;

    private static final String DEFAULT_BEGIN = "1999-03-22";
    private static final String DEFAULT_END = "now";


    public CheckListHolder() {
        begin = DEFAULT_BEGIN;
        end = DEFAULT_END;
        list = ChecksRoller.getInstance().getAppDatabase().getCheckDao().getAll();
    }

    /**
     * Converts date from DD.MM.YYYY to YYYY-MM-DD
     * terrible code (todo) fix it
     */
    private static String dateConvert(String date) throws IllegalArgumentException{
        String[] args = date.split("\\.");

        if (args.length != 3) {
            throw new IllegalArgumentException();
        }

        //:(
        String tmp = args[0];
        args[0] =  args[2];
        args[2] = tmp;

        if (!args[2].matches("[0-9]{2}") || !args[1].matches("[0-9]{2}")
                || !args[0].matches("[0-9]{4}")) {
            throw new IllegalArgumentException();
        }

        return args[0] + "-" + args[1] + "-" + args[2];
    }

    public void setBegin(@NonNull String begin) {
        if (begin.equals("")) {
            this.begin = DEFAULT_BEGIN;
        } else {
            this.begin = dateConvert(begin);
        }
        updateStateByTime();
    }

    public void setEnd(@NonNull String end) {
        if (end.equals("")) {
            this.end = DEFAULT_END;
        } else {
            this.end = dateConvert(end);
        }
        updateStateByTime();
    }

    public void setSubstring( String substring) {
        Log.i(ChecksRoller.LOG_TAG, substring + "");
        regEx = "%" + substring + "%";
        updateStateBySubstring();
    }

    private void updateStateBySubstring() {
        list.clear();
        Log.i(ChecksRoller.LOG_TAG, "Looking for " + regEx);
        list.addAll(ChecksRoller.getInstance().findCheckBySubstring(regEx));
    }

    private void updateStateByTime() {
        list.clear();
        Log.i(ChecksRoller.LOG_TAG, "Looking between " + begin + " " + end);
        List<CheckWithProducts> list1 = ChecksRoller.getInstance().findCheckByTimePeriod(begin, end);
        list.addAll(list1);
    }

    private void updateState() {
        list.clear();
        if (regEx.equals("%%") && begin.equals(DEFAULT_BEGIN) && end.equals(DEFAULT_END)) {
            list.addAll(ChecksRoller.getInstance().getAppDatabase().getCheckDao().getAll());
        }
        Log.i(ChecksRoller.LOG_TAG, "Looking between " + begin + " " + end + " by " + regEx);
        List<CheckWithProducts> list1 = ChecksRoller.getInstance().findChecksByTimePeriodAndRegEx(begin, end, regEx);
        list.addAll(list1);
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
