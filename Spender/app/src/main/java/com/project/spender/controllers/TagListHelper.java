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
import com.project.spender.roller.App;
import com.project.spender.roller.ChecksRoller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Class to control all tags showing activity
 */
public class TagListHelper {

    @Inject protected ChecksRoller checksRoller;

    private ListView listView;
    private List<Tag> tags;
    private TextView info;

    /**
     * Creates new instance
     * @param context owner
     * @param listView view for tags list
     * @param textView info showing in empty list case
     */
    public TagListHelper(Context context, ListView listView, TextView textView) {
        this.listView = listView;
        info = textView;
        tags = new ArrayList<>();
        listView.setAdapter(new TagChoiceAdapter(context, tags, null));
        update((LifecycleOwner) context);
        App.getComponent().inject(this);
    }

    /**
     * Removes element with given index
     */
    public void remove(int i) {
        checksRoller.deleteTag(tags.get(i).getId());
    }

    /**
     * Updates all tags list
     */
    public void update(LifecycleOwner owner) {
        LiveData<List<Tag>> list = checksRoller.getAppDatabase().getCheckDao().getAllTags();
        list.observe(owner, this::updateList);
    }

    /**
     * Replaces tags list with given list.
     * If its empty, shows info
     */
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

    /**
     * Returns default tag view parameters
     */
    public static LinearLayout.LayoutParams tagParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(50, 30);
        params.setMargins(5, 0, 5, 0);
        return params;
    }
}
