package com.project.spender.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.project.spender.R;
import com.project.spender.data.entities.Tag;

import java.util.List;

/**
 * Adapter for products list
 */
public class TagAdapter extends BaseAdapter {
    private LayoutInflater lInflater;
    private List<Tag> tagsList;

    TagAdapter(Context context, List<Tag> tags) {
        tagsList = tags;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return tagsList.size();
    }

    @Override
    public Object getItem(int position) {
        return tagsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = lInflater.inflate(R.layout.tag_view, parent, false);
        }

        final View view = convertView;

        Tag tag = getProduct(position);

        View color = view.findViewById(R.id.color);
        color.setBackgroundColor(tag.getColor());

        return view;
    }

    private Tag getProduct(int position) {
        return (Tag) getItem(position);
    }

}

