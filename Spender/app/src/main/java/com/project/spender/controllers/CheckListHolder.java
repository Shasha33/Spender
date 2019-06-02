package com.project.spender.controllers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.project.spender.activities.CheckShowActivity;
import com.project.spender.adapters.ItemAdapter;
import com.project.spender.adapters.ListAdapter;
import com.project.spender.data.entities.CheckWithProducts;
import com.project.spender.data.entities.Product;
import com.project.spender.data.entities.ProductTagJoin;
import com.project.spender.data.entities.Tag;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.project.spender.controllers.DataHelper.dateConvert;

public class CheckListHolder {

    @NonNull private String regEx;
    @NonNull private String begin;
    @NonNull private String end;
    private List<CheckWithProducts> list;
    private List<Product> productList;
    private boolean isProductMode = false;
    private ListView listView;
    private List<Long> tags;
    private int chosenPos;
    private ListAdapter checksAdapter;
    private ItemAdapter productsAdapter;

    LifecycleOwner owner;


    public CheckListHolder(ListView listView, Context context) {
        begin = DataHelper.DEFAULT_BEGIN;
        end = DataHelper.DEFAULT_END;
        regEx = "%%";
        list = new ArrayList<>();
        productList = new ArrayList<>();

        owner = (LifecycleOwner) context;

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

        updateState();
    }

    public void chooseItem(int index) {
        chosenPos = index;
    }

    public void addTagsForCheck(long[] ids) {
        if (chosenPos == -1) {
            return;
        }
        addTags(chosenPos, ids);
    }

    public void removeTagsForCheck(long[] ids) {
        if (chosenPos == -1) {
            return;
        }
        removeTags(chosenPos, ids);
    }

    public void changeMode() {
        isProductMode = !isProductMode;
        chosenPos = -1;
        if (isProductMode) {
            listView.setAdapter(productsAdapter);
        } else {
            listView.setAdapter(checksAdapter);
        }
    }

    public void setBegin(@NonNull String begin) {
        try {
            this.begin = dateConvert(begin);
        } catch (ParseException e) {
            this.begin = DataHelper.DEFAULT_BEGIN;
        }
        updateState();
    }

    public void setEnd(@NonNull String end) {
        try {

            this.end = dateConvert(end);
        } catch (ParseException e) {
            this.end = DataHelper.DEFAULT_END;
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
            for (Product p : productList) {
                Log.i(ChecksRoller.LOG_TAG, "" + p.getName());
            }
        } else {
            updateStateCheck();
            for (CheckWithProducts p : list) {
                Log.i(ChecksRoller.LOG_TAG, "" + p.getCheck().getName());
            }
        }
        listView.invalidateViews();
    }

    //its terrible (todo) fix it
    public void setTags(@NonNull long[] ids) {
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

    private <T> void addIfContain(List<T> list1, List<Tag> tags1, T o) {
        for (Tag tag : tags1) {
            if (tags.contains(tag.getId())) {
                list1.add(o);
                break;
            }
        }
        listView.invalidateViews();
    }

    private void updateStateCheckByList(List<CheckWithProducts> list1) {
        if (tags == null) {
            list.addAll(list1);
            listView.invalidateViews();
            return;
        }
        for (CheckWithProducts check : list1) {
            LiveData<List<Tag>> tagList = ChecksRoller.getInstance().getAppDatabase().getCheckDao().getTagsByCheckId(check.getCheck().getId());
//            Log.i(ChecksRoller.LogG_TAG, );
            tagList.observe(owner, tags -> addIfContain(list, tags, check));
        }
    }

    private void updateStateProductByList(List<Product> list1) {
        if (tags == null) {
            productList.addAll(list1);
            listView.invalidateViews();
            return;
        }
        for (Product product: list1) {
            LiveData<List<Tag>> tagList = ChecksRoller.getInstance().getAppDatabase().getCheckDao().getTagsByProductId(product.getId());
            tagList.observe(owner, tags -> addIfContain(productList, tags, product));
        }
    }

    private void updateStateCheck() {
        list.clear();
        LiveData<List<CheckWithProducts>> list1 = ChecksRoller.getInstance().findChecksByTimePeriodAndRegEx(begin, end, regEx);
        list1.observe(owner, this::updateStateCheckByList);
    }

    private void updateStateProduct() {
        productList.clear();
        LiveData<List<Product>> list1 = ChecksRoller.getInstance().getAppDatabase().getCheckDao().getProductsByRegEx(regEx);
        list1.observe(owner, this::updateStateProductByList);
    }

    private void addTags(int position, long[] tagIds) {
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

    private void removeTags(int position, long[] tagIds) {
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
