package com.project.spender.activities;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.project.spender.R;
import com.project.spender.data.entities.Product;

import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter {
    Context context;
    LayoutInflater lInflater;
    ArrayList<Product> productList;

    ItemAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        productList = products;
        lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = lInflater.inflate(R.layout.item_view, parent, false);
        }

        final View view = convertView;

        Product product = getProduct(position);

        ((TextView) view.findViewById(R.id.name)).setText(product.getName());
        ((TextView) view.findViewById(R.id.price)).setText("price: "  +
                String.format("%.2f", product.getPrice() / 100.0));
        ((TextView) view.findViewById(R.id.count)).setText("quantity: " + product.getQuantity());

        return view;
    }

    Product getProduct(int position) {
        return (Product) getItem(position);
    }

}

