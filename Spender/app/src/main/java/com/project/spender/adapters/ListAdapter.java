package com.project.spender.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.project.spender.roller.App;
import com.project.spender.roller.ChecksRoller;
import com.project.spender.R;
import com.project.spender.controllers.TagListHelper;
import com.project.spender.data.entities.Check;
import com.project.spender.data.entities.CheckWithProducts;
import com.project.spender.data.entities.Tag;

import java.util.List;

import javax.inject.Inject;

/**
 * Adapter for checks list
 */
public class ListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<CheckWithProducts> checkList;
    private LifecycleOwner owner;
    @Inject protected ChecksRoller checksRoller;

    public ListAdapter(Context context, List<CheckWithProducts> list) {
        this.context = context;
        checkList = list;
        owner = (LifecycleOwner) context;
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        App.getComponent().inject(this);
    }

    @Override
    public int getCount() {
        return checkList.size();
    }

    @Override
    public Object getItem(int position) {
        return checkList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.check_list_view, parent, false);
        }

        final View view = convertView;

        Check check = getCheck(position);


        LiveData<List<Tag>> tags = checksRoller.getAppDatabase()
                .getCheckDao().getTagsByCheckId(check.getId());
        final LinearLayout linearLayout = view.findViewById(R.id.check_tag_list);
        tags.observe(owner, tags1 -> updateTags(tags1, linearLayout));


        ((TextView) view.findViewById(R.id.name)).setText(check.getName());
        ((TextView) view.findViewById(R.id.sum)).setText("Total: "  +
                String.format("%.2f", check.getTotalSum() / 100.0));
        ((TextView) view.findViewById(R.id.data)).setText("Time: " + check.getDate().replace("T", " "));
        ((TextView) view.findViewById(R.id.shop)).setText("Shop: " + check.getShop());

        return view;
    }

    private Check getCheck(int position) {
        return ((CheckWithProducts) getItem(position)).getCheck()   ;
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

