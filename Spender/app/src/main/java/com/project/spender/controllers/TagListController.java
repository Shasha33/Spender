package com.project.spender.controllers;

import android.content.Context;
import android.widget.ListView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.project.spender.ChecksRoller;
import com.project.spender.adapters.TagChoiceAdapter;
import com.project.spender.data.entities.Tag;

import java.util.List;

public class TagListController {

    private ListView listView;
    private List<Tag> tags;

    public TagListController(Context context, ListView listView) {
        this.listView = listView;
        listView.setAdapter(new TagChoiceAdapter(context, tags));
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
}
