package com.project.spender.activities;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.project.spender.ChecksRoller;
import com.project.spender.R;
import com.project.spender.data.entities.Product;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

public class CheckShowActivity extends AppCompatActivity {

    private ArrayList<Product> products;
    private ListView listView;

    private HashSet<Product> productsForAction;

    private static final int SELECTED_ITEM = Color.rgb(0, 255, 127);

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.products_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.remove:
                return true;

            case R.id.add_tag:
                return true;

            default:

                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_show);


        products = getIntent().getParcelableArrayListExtra("products");
        productsForAction = new HashSet<>();

        listView = findViewById(R.id.productsList);
        listView.setAdapter(new ItemAdapter(this, products));

        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (productsForAction.contains(products.get(position))) {
                productsForAction.remove(products.get(position));
                view.setBackgroundColor(Color.WHITE);
            } else {
                productsForAction.add(products.get(position));
                view.setBackgroundColor(SELECTED_ITEM);
            }
        });
    }
}
