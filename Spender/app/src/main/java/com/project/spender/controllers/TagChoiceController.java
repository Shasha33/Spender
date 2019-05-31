package com.project.spender.controllers;

import android.content.Context;
import android.content.Intent;
import android.widget.ListView;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.project.spender.ChecksRoller;
import com.project.spender.adapters.TagChoiceAdapter;
import com.project.spender.data.entities.Tag;

import java.util.ArrayList;
import java.util.List;

import static com.project.spender.controllers.CheckShowController.SELECTED_ITEM;
import static com.project.spender.controllers.CheckShowController.UNSELECTED_ITEM;

public class TagChoiceController {

    private List<Tag> clickedTags;
    private List<Tag> tags;
    private ListView listView;

    public static final String TAG_ID_LIST = "vlad molodec";

    public TagChoiceController(Context context, ListView listView) {
        this.listView = listView;
        clickedTags = new ArrayList<>();
        tags = new ArrayList<>();

        LiveData<List<Tag>> ts = ChecksRoller.getInstance().getAppDatabase().getCheckDao().getAllTags();
        ts.observe((LifecycleOwner) context, this::setTags);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Tag current = tags.get(position);
            if (clickedTags.contains(current)) {
                clickedTags.remove(current);
                view.setBackgroundColor(UNSELECTED_ITEM);
            } else {
                clickedTags.add(current);
                view.setBackgroundColor(SELECTED_ITEM);
            }
        });
        listView.setAdapter(new TagChoiceAdapter(context, tags));


    }

    private void setTags(List<Tag> list) {
        tags.clear();
        tags.addAll(list);
        listView.invalidateViews();

    }

    public Intent resultIntent() {
        Intent intent = new Intent();
        long[] ids = new long[clickedTags.size()];
        for (int i = 0; i < clickedTags.size(); i++) {
            ids[i] = clickedTags.get(i).getId();
        }
        intent.putExtra(TAG_ID_LIST, ids);
        return intent;
    }

}
