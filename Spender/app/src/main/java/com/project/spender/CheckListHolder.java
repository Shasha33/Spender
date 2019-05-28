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

import static com.project.spender.DataHelper.dateConvert;

public class CheckListHolder {

    @NonNull private String regEx;
    @NonNull private String begin;
    @NonNull private String end;
    private List<CheckWithProducts> list;



    public CheckListHolder() {
        begin = DataHelper.DEFAULT_BEGIN;
        end = DataHelper.DEFAULT_END;
        list = ChecksRoller.getInstance().getAppDatabase().getCheckDao().getAll();
    }


    public void setBegin(@NonNull String begin) {
        if (begin.equals("")) {
            this.begin = DataHelper.DEFAULT_BEGIN;
        } else {
            this.begin = dateConvert(begin);
        }
        updateState();
    }

    public void setEnd(@NonNull String end) {
        if (end.equals("")) {
            this.end = DataHelper.DEFAULT_END;
        } else {
            this.end = dateConvert(end);
        }
        updateState();
    }

    public void setSubstring( String substring) {
        Log.i(ChecksRoller.LOG_TAG, substring + "");
        regEx = "%" + substring + "%";
        updateState();
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
        if (regEx.equals("%%") && begin.equals(DataHelper.DEFAULT_BEGIN) && end.equals(DataHelper.DEFAULT_END)) {
            list.addAll(ChecksRoller.getInstance().getAppDatabase().getCheckDao().getAll());
        }
        Log.i(ChecksRoller.LOG_TAG, "Looking between " + begin + " " + end + " by " + regEx);
        List<CheckWithProducts> list1 = ChecksRoller.getInstance().findChecksByTimePeriodAndRegEx(begin, end, regEx);
        list.addAll(list1);
    }

    public void addTags(int position, long[] tagIds) {
        for (long id : tagIds) {
            addTag(position, id);
        }
    }

    public void addTag(int position, long tagId) {
        for (Product product : list.get(position).getProducts()) {
            ChecksRoller.getInstance().getAppDatabase()
                        .getCheckDao().insertExistingTagForProduct(tagId, product.getId());
        }
    }

    public void removeTags(int position, long[] tagIds) {
        for (long id : tagIds) {
            removeTag(position, id);
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
