package com.project.spender.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.gms.vision.L;
import com.project.spender.R;
import com.project.spender.data.entities.Tag;

import java.util.List;

import static com.project.spender.controllers.CheckShowHelper.SELECTED_ITEM;
import static com.project.spender.controllers.CheckShowHelper.UNSELECTED_ITEM;

/**
 * Adapter for tags available to choose list
 */
public class TagChoiceAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private @Nullable List<Tag> tagList;
    private List<Tag> chosenTags;

    public TagChoiceAdapter(Context context, List<Tag> list, List<Tag> chosen) {
        chosenTags = chosen;
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

        if (chosenTags != null) {
            if (chosenTags.contains(tag)) {
                convertView.setBackgroundColor(SELECTED_ITEM);
            } else {
                convertView.setBackgroundColor(UNSELECTED_ITEM);
            }
        }



        ((TextView) convertView.findViewById(R.id.tag_in_list_name)).setText(tag.getName());
        if (tag.getSubstring() != null) {
            ((TextView) convertView.findViewById(R.id.tag_in_list_substring)).setText("auto adding by " + tag.getSubstring());
        }
        convertView.findViewById(R.id.tag_in_list_color).setBackgroundColor(tag.getColor());


        return convertView;
    }
}

