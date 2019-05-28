package com.project.spender.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.spender.ChecksRoller;
import com.project.spender.R;
import com.project.spender.data.entities.Product;
import com.project.spender.data.entities.ProductTagJoin;

import java.util.ArrayList;
import java.util.HashSet;

public class CheckShowActivity extends AppCompatActivity {

    private ArrayList<Product> products;
    private ListView listView;
    private EditText search;


    private static final int ADDING_CODE = 20;
    private static final int REMOVING_CODE = 40;

    private HashSet<Product> productsForAction;

    private static final int SELECTED_ITEM = Color.rgb(0, 255, 127);

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        long tag = data.getLongExtra("tag id", -1);
        if (tag == -1) {
            return;
        }
        switch (requestCode) {
            case ADDING_CODE:
                for (Product product : productsForAction) {
                    ChecksRoller.getInstance().getAppDatabase()
                            .getCheckDao().insertExistingTagForProduct(tag, product.getId());
                }
                listView.invalidateViews();
                productsForAction.clear();
                return;
            case REMOVING_CODE:
                Toast.makeText(this, "Heh", Toast.LENGTH_SHORT).show();
                for (Product product : productsForAction) {
                    System.out.println(product.getName() + " " + product.getId() + " " + tag);
                    ChecksRoller.getInstance().getAppDatabase()
                            .getCheckDao()
                            .deleteTagProductRelation(new ProductTagJoin(product.getId(), tag));
                }
                listView.invalidateViews();
                productsForAction.clear();
                return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.products_menu, menu);
        return true;
    }

    private int getRequestCode(int id) {
        switch (id) {
            case R.id.remove_tag:
                return REMOVING_CODE;
            case R.id.add_tag:
                return ADDING_CODE;
        }
        return -1;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_tag:
                Intent intent = new Intent(this, NewTagActivity.class);
                startActivity(intent);
                return true;

            case R.id.remove:
                for (Product product : productsForAction) {
                    ChecksRoller.getInstance().getAppDatabase().getCheckDao().deleteProductById(product.getId());
                    products.remove(product);
                }
                listView.invalidateViews();
                productsForAction.clear();
                return true;

            case R.id.add_tag:
            case R.id.remove_tag:
                Intent intentTagChoice = new Intent(this, TagChoiceActivity.class);
                startActivityForResult(intentTagChoice, getRequestCode(item.getItemId()));
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
        long checkId = getIntent().getLongExtra("check id", -1);
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

        search = findViewById(R.id.search_in_check);
        search.setOnEditorActionListener((v, actionId, event) -> {
            products.clear();
            products.addAll(ChecksRoller.getInstance().findProductsInCheckBySubstring(checkId, search.getText().toString()));
            listView.invalidateViews();
            return true;
        });
    }
}
