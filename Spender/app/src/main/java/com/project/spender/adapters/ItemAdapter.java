package com.project.spender.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.vision.L;
import com.project.spender.ChecksRoller;
import com.project.spender.R;
import com.project.spender.controllers.TagListController;
import com.project.spender.data.entities.Product;
import com.project.spender.data.entities.Tag;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ItemAdapter extends BaseAdapter {
    Context context;
    LayoutInflater lInflater;
    List<Product> productList;
    LinearLayout layout;
    LifecycleOwner owner;

    public ItemAdapter(Context context, List<Product> products) {
        this.context = context;
        owner = (LifecycleOwner) context;
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
        ((TextView) view.findViewById(R.id.price)).setText("Price: "  +
                String.format("%.2f", product.getPrice() / 100.0));
        ((TextView) view.findViewById(R.id.count)).setText("Quantity: " + product.getQuantity());

        layout = view.findViewById(R.id.linear_layout);

        LiveData<List<Tag>> tags = ChecksRoller.getInstance().getAppDatabase().getCheckDao().getTagsByProductId(product.getId());
//        Log.i(ChecksRoller.LOG_TAG, product.getName());
        final LinearLayout linearLayout = layout;
        tags.observe(owner, tags1 -> {
            updateTags(tags1, linearLayout);
            for (Tag t : tags1) {
                Log.i(ChecksRoller.LOG_TAG, product.getName() + " " + t);
            }
        });



        return view;
    }

    Product getProduct(int position) {
        return (Product) getItem(position);
    }

    private void updateTags(List<Tag> tags, LinearLayout layout1) {

        Adapter adapter = new TagAdapter(context, tags);
        layout1.removeAllViews();
        for (int i = 0; i < tags.size(); i++) {
            View child = adapter.getView(i, null, null);
            child.setBackgroundColor(tags.get(i).getColor());
//            Log.i(ChecksRoller.LOG_TAG, "" + tags.get(i));
            layout1.addView(child, TagListController.tagParams());
        }
        layout1.invalidate();
    }

}

