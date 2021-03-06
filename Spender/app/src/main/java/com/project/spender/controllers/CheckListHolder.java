package com.project.spender.controllers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.project.spender.activities.CheckShowActivity;
import com.project.spender.adapters.ItemAdapter;
import com.project.spender.adapters.ListAdapter;
import com.project.spender.data.entities.CheckWithProducts;
import com.project.spender.data.entities.Product;
import com.project.spender.data.entities.Tag;
import com.project.spender.roller.App;
import com.project.spender.roller.ChecksRoller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.project.spender.controllers.DateHelper.dateConvert;

public class CheckListHolder {

    @Inject protected ChecksRoller checksRoller;

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
    private AdapterView.OnItemClickListener checkListener;
    private LifecycleOwner owner;


    public CheckListHolder(ListView listView, Context context, TextView textView) {
        App.getComponent().inject(this);

        begin = DateHelper.DEFAULT_BEGIN;
        end = DateHelper.DEFAULT_END;
        regEx = "%%";
        list = new ArrayList<>();
        productList = new ArrayList<>();

        textView.setVisibility(View.INVISIBLE);
        owner = (LifecycleOwner) context;

        this.listView = listView;
        checksAdapter = new ListAdapter(context, list);
        productsAdapter = new ItemAdapter(context, productList, null);
        listView.setAdapter(checksAdapter);
        checkListener = (adapterView, view, i, l) -> {
            CheckWithProducts check = list.get(i);
            Intent intent = new Intent(context, CheckShowActivity.class);
            intent.putParcelableArrayListExtra("products",
                    (ArrayList<Product>) check.getProducts());
            intent.putExtra("check id", check.getCheck().getId());
            context.startActivity(intent);
        };
        listView.setOnItemClickListener(checkListener);

        updateState();
        System.out.println(checksRoller);
    }

    public void chooseItem(int index) {
        chosenPos = index;
    }

    private void addTagsForCheck(long[] ids) {
        if (chosenPos == -1) {
            return;
        }
        addTags(chosenPos, ids);
    }

    private void removeTagsForCheck(long[] ids) {
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
            listView.setOnItemClickListener((adapterView, view, i, l) -> {});
        } else {
            listView.setAdapter(checksAdapter);
            listView.setOnItemClickListener(checkListener);
        }
        updateState();
    }

    public void setBegin(@NonNull String begin) {
        try {
            this.begin = dateConvert(begin);
        } catch (ParseException e) {
            this.begin = DateHelper.DEFAULT_BEGIN;
            if (!begin.equals("")) {
                throw new IllegalArgumentException();
            }
        }
        updateState();
    }

    public void setEnd(@NonNull String end) {
        try {
            this.end = dateConvert(end);
        } catch (ParseException e) {
            this.end = DateHelper.DEFAULT_END;
            if (!end.equals("")) {
                throw new IllegalArgumentException();
            }
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
            Log.i(ChecksRoller.LOG_TAG, productList.size() + " is product");
        } else {
            updateStateCheck();
            for (CheckWithProducts p : list) {
                Log.i(ChecksRoller.LOG_TAG, "" + p.getCheck().getName());
            }

            Log.i(ChecksRoller.LOG_TAG, list.size() + " is check");
        }

        listView.invalidateViews();
    }

    //its terrible (todo) fix it
    public void setTags(@NonNull long[] ids) {
        if (ids.length == 0){
            tags = null;
            return;
        }
        tags = new ArrayList<>();
        for (long id : ids) {
            Log.i(ChecksRoller.LOG_TAG, id + "");
            tags.add(id);
        }
        updateState();
    }

    public void addTagsForItem(long[] ids) {
        if (chosenPos == -1) {
            return;
        }

        if (isProductMode) {
            Product product = productList.get(chosenPos);
            for (long i : ids) {
                checksRoller.insertTagForProductById(i, product.getId());
            }
        } else {
            addTagsForCheck(ids);
        }

        chosenPos = -1;
    }

    public void removeTagsForItem(long[] ids) {
        if (chosenPos == -1) {
            return;
        }

        if (isProductMode) {
            Product product = productList.get(chosenPos);
            for (long i : ids) {
                checksRoller.deleteTagForProduct(i, product.getId());
            }
        } else {
            removeTagsForCheck(ids);
        }
        chosenPos = -1;
    }

    private void removeCheck(int i) {
        checksRoller.deleteCheck(list.get(i).getCheck().getId());
        list.remove(i);
    }

    private void removeProduct(int i) {
        checksRoller.deleteProduct(productList.get(i).getId());
        productList.remove(i);
    }

    public void removeItem(int i) {
        if (isProductMode) {
            removeProduct(i);
        } else {
            removeCheck(i);
        }
        listView.invalidateViews();
    }

    public boolean getMode() {
        return isProductMode;
    }

    private synchronized <T> void addIfContain(List<T> list1, List<Tag> tags1, T o) {
        for (Tag tag : tags1) {
            if (tags.contains(tag.getId())) {
                list1.add(o);
                break;
            }
        }
        listView.invalidateViews();
    }

    private synchronized void updateStateCheckByList(List<CheckWithProducts> list1) {
        if (tags == null) {
            list.clear();
            list.addAll(list1);
            listView.invalidateViews();
            return;
        }
        for (CheckWithProducts check : list1) {
            LiveData<List<Tag>> tagList = checksRoller.getAppDatabase().getCheckDao().getTagsByCheckId(check.getCheck().getId());
//            Log.i(ChecksRoller.LogG_TAG, );
            tagList.observe(owner, tags -> addIfContain(list, tags, check));
        }
    }

    private synchronized void updateStateProductByList(List<Product> list1) {
        if (tags == null) {
            productList.clear();
            productList.addAll(list1);
            listView.invalidateViews();
            return;
        }
        for (Product product: list1) {
            LiveData<List<Tag>> tagList = checksRoller.getAppDatabase().getCheckDao().getTagsByProductId(product.getId());
            tagList.observe(owner, tags -> addIfContain(productList, tags, product));
        }
    }

    private void updateStateCheck() {
        list.clear();
        LiveData<List<CheckWithProducts>> list1 = checksRoller.findChecksByTimePeriodAndRegEx(begin, end, regEx);
        list1.observe(owner, this::updateStateCheckByList);
    }

    private void updateStateProduct() {
        productList.clear();
        LiveData<List<Product>> list1 = checksRoller.getAppDatabase().getCheckDao().getProductsByRegEx(regEx);
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

    private void addTagForCheck(int position, long tagId) {
        for (Product product : list.get(position).getProducts()) {
            checksRoller.insertTagForProductById(tagId, product.getId());
        }
    }

    private void addTagForProduct(int position, long tagId) {
        checksRoller.insertTagForProductById(tagId, productList.get(position).getId());
    }

    private void removeTags(int position, long[] tagIds) {
        for (long id : tagIds) {
            removeTag(position, id);
        }
    }

    private void removeTag(int position, long tagId) {
        for (Product product : list.get(position).getProducts()) {
            checksRoller.deleteTagForProduct(tagId, product.getId());
        }
    }

    public List<CheckWithProducts> getList() {
        return list;
    }
}
