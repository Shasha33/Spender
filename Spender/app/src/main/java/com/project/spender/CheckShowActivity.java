package com.project.spender;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.project.spender.data.entities.Product;

import java.util.ArrayList;
import java.util.List;

public class CheckShowActivity extends AppCompatActivity {

    private List<String> products;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_show);

        products = new ArrayList<>();
        for (Product i : ListActivity.getProductList()) {
            products.add(i.getName());
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.list_view, products);

        listView = findViewById(R.id.productsList);
        listView.setAdapter(adapter);
    }
}
