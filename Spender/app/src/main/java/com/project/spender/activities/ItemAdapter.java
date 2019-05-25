package com.project.spender.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.project.spender.ChecksRoller;
import com.project.spender.R;
import com.project.spender.data.entities.Product;
import com.project.spender.data.entities.Tag;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ItemAdapter extends BaseAdapter {
    Context context;
    LayoutInflater lInflater;
    List<Product> productList;

    ItemAdapter(Context context, List<Product> products) {
        this.context = context;
        productList = products;
        lInflater = (LayoutInflater) context
                .getSystemService(LAYOUT_INFLATER_SERVICE);
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

        //(todo) update checkroller to make this line shorter
        List<Tag> tags = ChecksRoller.getInstance().getAppDatabase().getCheckDao().getTagsByProductId(product.getId());
        System.out.println(product.getName() + " " + product.getId());
        for (Tag t : tags) {
            System.out.println(t.getName());
        }


        LinearLayout layout = view.findViewById(R.id.linear_layout);
        Adapter adapter = new TagAdapter(context, tags);
        for (int i = 0; i < tags.size(); i++) {
            System.out.println(i);
            View child = adapter.getView(i, null, null);
            if (i % 3 == 0) {
                child.setBackgroundColor(Color.RED);
            } else if (i % 3 == 2) {
                child.setBackgroundColor(Color.GREEN);
            } else {
                child.setBackgroundColor(Color.CYAN);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(30, 30);
            params.setMargins(5, 0, 5, 0);
            layout.addView(child, params);
        }

        return view;
    }

    Product getProduct(int position) {
        return (Product) getItem(position);
    }

}

