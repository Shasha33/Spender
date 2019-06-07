package com.project.spender.controllers;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.project.spender.adapters.TagChoiceAdapter;
import com.project.spender.data.entities.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagListHelper {

    private ListView listView;
    private List<Tag> tags;
    private TextView info;

    public TagListHelper(Context context, ListView listView, TextView textView) {
        this.listView = listView;
        info = textView;
        tags = new ArrayList<>();
        listView.setAdapter(new TagChoiceAdapter(context, tags, null));
        update((LifecycleOwner) context);
    }

    public void remove(int i) {
        ChecksRoller.getInstance().getAppDatabase().getCheckDao().deleteTagById(tags.get(i).getId());
    }

    public void update(LifecycleOwner owner) {
        LiveData<List<Tag>> list = ChecksRoller.getInstance().getAppDatabase().getCheckDao().getAllTags();
        list.observe(owner, this::updateList);
    }

    private void updateList(List<Tag> list) {
        tags.clear();
        tags.addAll(list);
        if (tags.size() == 0) {
            info.setVisibility(View.VISIBLE);
        } else {
            info.setVisibility(View.INVISIBLE);
        }
        listView.invalidateViews();
    }

    public static LinearLayout.LayoutParams tagParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(50, 30);
        params.setMargins(5, 0, 5, 0);
        return params;
    }
}
