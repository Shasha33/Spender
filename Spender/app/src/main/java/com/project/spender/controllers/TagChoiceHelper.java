package com.project.spender.controllers;

import android.content.Context;
import android.content.Intent;
import android.widget.ListView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.project.spender.adapters.TagChoiceAdapter;
import com.project.spender.data.entities.Tag;
import com.project.spender.roller.App;
import com.project.spender.roller.ChecksRoller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.project.spender.controllers.CheckShowHelper.SELECTED_ITEM;
import static com.project.spender.controllers.CheckShowHelper.UNSELECTED_ITEM;

/**
 * Class to control tag choice process
 */
public class TagChoiceHelper {

    @Inject private ChecksRoller checksRoller;

    private List<Tag> clickedTags;
    private List<Tag> tags;
    private ListView listView;

    public static final String TAG_ID_LIST = "vlad molodec";

    /**
     * Creates new instance with given list view for all tags list, using context as owner
     */
    public TagChoiceHelper(Context context, ListView listView) {
        this.listView = listView;
        clickedTags = new ArrayList<>();
        tags = new ArrayList<>();

        LiveData<List<Tag>> ts = checksRoller.getAppDatabase().getCheckDao().getAllTags();
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
        listView.setAdapter(new TagChoiceAdapter(context, tags, clickedTags));

        App.getComponent().inject(this);
    }

    private void setTags(List<Tag> list) {
        tags.clear();
        tags.addAll(list);
        listView.invalidateViews();

    }

    /**
     * Created return intent with extra tags list in it
     */
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
