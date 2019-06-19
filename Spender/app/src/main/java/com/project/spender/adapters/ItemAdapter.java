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

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.project.spender.controllers.ChecksRoller;
import com.project.spender.R;
import com.project.spender.controllers.TagListHelper;
import com.project.spender.data.entities.Product;
import com.project.spender.data.entities.Tag;

import java.util.HashSet;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.project.spender.controllers.CheckShowHelper.SELECTED_ITEM;
import static com.project.spender.controllers.CheckShowHelper.UNSELECTED_ITEM;

/**
 * Adapter for products list
 */
public class ItemAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater lInflater;
    private List<Product> productList;
    private LifecycleOwner owner;
    @Nullable private HashSet<Integer> chosen;

    public ItemAdapter(Context context, List<Product> products, @Nullable HashSet<Integer> hashSet) {
        chosen = hashSet;
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
        } else if (chosen != null) {
            if (chosen.contains(position)) {
                convertView.setBackgroundColor(SELECTED_ITEM);
            } else {
                convertView.setBackgroundColor(UNSELECTED_ITEM);
            }
        }

        final View view = convertView;

        Product product = getProduct(position);

        ((TextView) view.findViewById(R.id.name)).setText(product.getName());
        ((TextView) view.findViewById(R.id.price)).setText("Price: "  +
                String.format("%.2f", product.getPrice() / 100.0));
        ((TextView) view.findViewById(R.id.count)).setText("Quantity: " + product.getQuantity());

        LinearLayout layout = view.findViewById(R.id.linear_layout);

        LiveData<List<Tag>> tags = ChecksRoller.getInstance().getAppDatabase().getCheckDao().getTagsByProductId(product.getId());
        final LinearLayout linearLayout = layout;
        tags.observe(owner, tags1 -> {
            updateTags(tags1, linearLayout);
            for (Tag t : tags1) {
                Log.i(ChecksRoller.LOG_TAG, product.getName() + " " + t);
            }
        });



        return view;
    }

    private Product getProduct(int position) {
        return (Product) getItem(position);
    }

    private void updateTags(List<Tag> tags, LinearLayout layout1) {

        Adapter adapter = new TagAdapter(context, tags);
        layout1.removeAllViews();
        for (int i = 0; i < tags.size(); i++) {
            View child = adapter.getView(i, null, null);
            child.setBackgroundColor(tags.get(i).getColor());
            layout1.addView(child, TagListHelper.tagParams());
        }
        layout1.invalidate();
    }

}

