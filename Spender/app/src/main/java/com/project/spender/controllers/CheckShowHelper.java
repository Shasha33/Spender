package com.project.spender.controllers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.mattyork.colours.Colour;
import com.project.spender.adapters.ItemAdapter;
import com.project.spender.data.entities.Product;
import com.project.spender.roller.App;
import com.project.spender.roller.ChecksRoller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

/**
 * Class to control CheckShow activity view
 */
public class CheckShowHelper {

    @Inject protected ChecksRoller checksRoller;
    private List<Product> products;
    private HashSet<Integer> productsForAction;
    private ListView listView;
    private String substring;
    private long checkId;

    private LifecycleOwner owner;

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
        App.getComponent().inject(this);
    }

    /**
     * Set up listener for given EditText to save its content to search parameter
     */
    public void setInput(EditText editText) {
        editText.setOnEditorActionListener((v, actionId, event) -> {
            substring = "%" + editText.getText().toString() + "%";
            update();
            return true;
        });

    }

    private void update() {
        LiveData<List<Product>> list = checksRoller.getAppDatabase().getCheckDao().getProductByRegEx(substring, checkId);
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

    /**
     * Adds tags for selected products
     */
    public void addTags(long[] tags) {
        for (int i : productsForAction) {
            for (long j : tags) {
                checksRoller.insertTagForProductById(j, products.get(i).getId());
            }
        }
        clearSelected();
    }

    /**
     * Remove tags for selected products
     */
    public void removeTags(long[] tags) {
        for (int i : productsForAction) {
            for (long j : tags) {
                checksRoller.deleteTagForProduct(j, products.get(i).getId());
            }
        }
        clearSelected();
    }

    /**
     * Removes selected products
     */
    public void removeProducts() {
        unColorSelected();
        for (int i : productsForAction) {
            checksRoller.deleteProduct(products.get(i).getId());
            products.remove(i);
        }
        clearSelectedSet();
        listView.invalidateViews();
    }
}
