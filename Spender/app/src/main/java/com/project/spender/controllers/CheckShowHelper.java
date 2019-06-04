package com.project.spender.controllers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.mattyork.colours.Colour;
import com.project.spender.adapters.ItemAdapter;
import com.project.spender.data.entities.Product;
import com.project.spender.data.entities.ProductTagJoin;

import java.util.HashSet;
import java.util.List;

public class CheckShowHelper {

    private List<Product> products;
    private HashSet<Integer> productsForAction;
    private ListView listView;
    private String substring;
    private long checkId;

    LifecycleOwner owner;

    public static final int SELECTED_ITEM = Colour.pastelGreenColor();
    public static final int UNSELECTED_ITEM = Color.WHITE;


    public CheckShowHelper(Context context, ListView listView, Intent intent) {
        productsForAction = new HashSet<>();
        owner = (LifecycleOwner) context;
        this.listView = listView;

        products = intent.getParcelableArrayListExtra("products");
        checkId = intent.getLongExtra("check id", -1);
        substring = "%%";

        listView.setAdapter(new ItemAdapter(context, products));

        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (productsForAction.contains(position)) {
                productsForAction.remove(position);
                view.setBackgroundColor(UNSELECTED_ITEM);
            } else {
                productsForAction.add(position);
                view.setBackgroundColor(SELECTED_ITEM);
            }
        });
    }

    public void setInput(EditText editText) {
        editText.setOnEditorActionListener((v, actionId, event) -> {
            substring = "%" + editText.getText().toString() + "%";
            update();
            return true;
        });

    }

    private void update() {
        LiveData<List<Product>> list = ChecksRoller.getInstance().getAppDatabase().getCheckDao().getProductByRegEx(substring, checkId);
        list.observe(owner, this::updateProducts);
    }

    private void updateProducts(List<Product> products) {
        this.products.clear();
        this.products.addAll(products);
        listView.invalidate();
    }

    private void unColorSelected() {
        for (int i : productsForAction) {
            listView.getChildAt(i). setBackgroundColor(UNSELECTED_ITEM);
        }
        listView.invalidate();
    }

    private void clearSelectedSet() {
        productsForAction.clear();
    }

    private void clearSelected() {

        unColorSelected();
        clearSelectedSet();
    }

    public void addTags(long[] tags) {
        for (int i : productsForAction) {
            for (long j : tags) {
                ChecksRoller.getInstance().getAppDatabase().getCheckDao().insertExistingTagForProduct(j, products.get(i).getId());
            }
        }
        //(todo) make sure about auto updating
        Log.i(ChecksRoller.LOG_TAG, "jere");
        clearSelected();
    }

    public void removeTags(long[] tags) {
        for (int i : productsForAction) {
            for (long j : tags) {
                ChecksRoller.getInstance().getAppDatabase().getCheckDao().deleteTagProductRelation(new ProductTagJoin(products.get(i).getId(), j));
            }
        }
        clearSelected();
    }

    public void removeProducts() {
        unColorSelected();
        for (int i : productsForAction) {
            ChecksRoller.getInstance().getAppDatabase().getCheckDao().deleteProductById(products.get(i).getId());
        }
        clearSelectedSet();
        update();
    }
}
