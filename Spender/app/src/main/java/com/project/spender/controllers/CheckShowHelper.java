package com.project.spender.controllers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.mattyork.colours.Colour;
import com.project.spender.adapters.ItemAdapter;
import com.project.spender.data.entities.Product;
import com.project.spender.data.entities.ProductTagJoin;

import java.util.ArrayList;
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

        products = new ArrayList<>();
        checkId = intent.getLongExtra("check id", -1);
        substring = "%%";

        listView.setAdapter(new ItemAdapter(context, products, productsForAction));

        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (productsForAction.contains(position)) {
                productsForAction.remove(position);
                view.setBackgroundColor(UNSELECTED_ITEM);
            } else {
                productsForAction.add(position);
                view.setBackgroundColor(SELECTED_ITEM);
            }
        });


        update();
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
        listView.invalidateViews();
    }

    private View getItemView(int position) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (position < firstListItemPosition || position > lastListItemPosition ) {
            return listView.getAdapter().getView(position, null, listView);
        } else {
            final int childIndex = position - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
    
    private void unColorSelected() {
        for (int i : productsForAction) {
            System.out.println(i);
            getItemView(i).setBackgroundColor(UNSELECTED_ITEM);
        }
        listView.invalidateViews();
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
            products.remove(i);
        }
        clearSelectedSet();
        listView.invalidateViews();
    }
}
