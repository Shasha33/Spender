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
import com.project.spender.data.entities.Check;
import com.project.spender.data.entities.CheckWithProducts;
import com.project.spender.data.entities.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagChoiceAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<Tag> tagList;

    TagChoiceAdapter(Context context, List<Tag> list) {
        this.context = context;
        tagList = list;
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return tagList.size();
    }

    @Override
    public Object getItem(int position) {
        return tagList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.tag_choice_item_view, parent, false);
        }

        Tag tag = tagList.get(position);

        ((TextView) convertView.findViewById(R.id.tag_in_list_name)).setText(tag.getName());
        convertView.findViewById(R.id.tag_in_list_name).setBackgroundColor(tag.getColor());


        return convertView;
    }
}

