package com.project.spender.controllers;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.project.spender.adapters.TagChoiceAdapter;
import com.project.spender.data.entities.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagListHelper {

    private ListView listView;
    private List<Tag> tags;

    public TagListHelper(Context context, ListView listView) {
        this.listView = listView;
        tags = new ArrayList<>();
        listView.setAdapter(new TagChoiceAdapter(context, tags));
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
        listView.invalidateViews();
    }

    public static LinearLayout.LayoutParams tagParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(50, 30);
        params.setMargins(5, 0, 5, 0);
        return params;
    }
}
