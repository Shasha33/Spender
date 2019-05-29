package com.project.spender;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.android.gms.vision.L;
import com.project.spender.activities.CheckShowActivity;
import com.project.spender.activities.ItemAdapter;
import com.project.spender.activities.ListAdapter;
import com.project.spender.data.entities.Check;
import com.project.spender.data.entities.CheckWithProducts;
import com.project.spender.data.entities.Product;
import com.project.spender.data.entities.ProductTagJoin;
import com.project.spender.data.entities.Tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.project.spender.DataHelper.dateConvert;

public class CheckListHolder {

    @NonNull private String regEx;
    @NonNull private String begin;
    @NonNull private String end;
    private List<CheckWithProducts> list;
    private List<Product> productList;
    private boolean isProductMode = false;
    private ListView listView;
    private List<Long> tags;
    private ListAdapter checksAdapter;
    private ItemAdapter productsAdapter;


    public CheckListHolder(ListView listView, Context context) {
        begin = DataHelper.DEFAULT_BEGIN;
        end = DataHelper.DEFAULT_END;
        regEx = "%%";
        list = ChecksRoller.getInstance().getAppDatabase().getCheckDao().getAll();
        productList = ChecksRoller.getInstance().getAppDatabase().getCheckDao().getAllProducts();

        this.listView = listView;
        checksAdapter = new ListAdapter(context, list);
        productsAdapter = new ItemAdapter(context, productList);
        listView.setAdapter(checksAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            CheckWithProducts check = list.get(position);
            Intent intent = new Intent(context, CheckShowActivity.class);
            intent.putParcelableArrayListExtra("products",
                    (ArrayList<Product>) check.getProducts());
            intent.putExtra("check id", check.getCheck().getId());
            context.startActivity(intent);
        });

    }

    public void changeMode() {
        isProductMode = !isProductMode;
        if (isProductMode) {
            listView.setAdapter(productsAdapter);
        } else {
            listView.setAdapter(checksAdapter);
        }
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

    private void updateState() {
        if (isProductMode) {
            updateStateProduct();
        } else {
            updateStateCheck();
        }
        listView.invalidateViews();
    }

    //its terrible (todo) fix it
    public void setTags(long[] ids) {
        tags = new ArrayList<>();
        for (long id : ids) {
            Log.i(ChecksRoller.LOG_TAG, id + "");
            tags.add(id);
        }
        updateState();
    }

    public boolean getMode() {
        return isProductMode;
    }

    private void updateStateCheck() {
        list.clear();
        if (regEx.equals("%%") && begin.equals(DataHelper.DEFAULT_BEGIN) && end.equals(DataHelper.DEFAULT_END)) {
            list.addAll(ChecksRoller.getInstance().getAppDatabase().getCheckDao().getAll());
        }
        Log.i(ChecksRoller.LOG_TAG, "Looking for checks between " + begin + " " + end + " by " + regEx);
        List<CheckWithProducts> list1 = ChecksRoller.getInstance().findChecksByTimePeriodAndRegEx(begin, end, regEx);
        if (tags == null) {
            list.addAll(list1);
            return;
        }
        for (CheckWithProducts check : list1) {
            List<Tag> tagList = ChecksRoller.getInstance().getAppDatabase().getCheckDao().getTagsByCheckId(check.getCheck().getId());
            for (Tag tag : tagList) {
                if (tags.contains(tag)) {
                    list.add(check);
                    break;
                }
            }
        }
    }

    private void updateStateProduct() {
        productList.clear();
        if (regEx.equals("%%")) {
            productList.addAll(ChecksRoller.getInstance().getAppDatabase().getCheckDao().getAllProducts());
        }
        Log.i(ChecksRoller.LOG_TAG, "Looking for products between " + begin + " " + end + " by " + regEx);
        List<Product> list1 = ChecksRoller.getInstance().getAppDatabase().getCheckDao().getProductsByRegEx(regEx);
        if (tags == null) {
            productList.addAll(list1);
            return;
        }
        for (Product product : list1) {
            List<Tag> tagList = ChecksRoller.getInstance().getAppDatabase().getCheckDao().getTagsByCheckId(product.getId());

            for (Tag tag : tagList) {
                if (tags.contains(tag)) {
                    productList.add(product);
                    break;
                }
            }
        }
    }

    public void addTags(int position, long[] tagIds) {
        for (long id : tagIds) {
            if (isProductMode) {
                addTagForProduct(position, id);
            } else {
                addTagForCheck(position, id);
            }
        }
    }

    public void addTagForCheck(int position, long tagId) {
        for (Product product : list.get(position).getProducts()) {
            ChecksRoller.getInstance().getAppDatabase()
                        .getCheckDao().insertExistingTagForProduct(tagId, product.getId());
        }
    }

    public void addTagForProduct(int position, long tagId) {
        ChecksRoller.getInstance().getAppDatabase()
                    .getCheckDao().insertExistingTagForProduct(tagId, productList.get(position).getId());
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
