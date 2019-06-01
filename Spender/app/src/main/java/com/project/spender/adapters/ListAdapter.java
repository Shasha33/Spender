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
import androidx.lifecycle.Observer;

import com.project.spender.ChecksRoller;
import com.project.spender.R;
import com.project.spender.controllers.TagListController;
import com.project.spender.data.entities.Check;
import com.project.spender.data.entities.CheckWithProducts;
import com.project.spender.data.entities.Tag;

import java.util.List;

public class ListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<CheckWithProducts> checkList;
    LinearLayout layout;
    LifecycleOwner owner;

    public ListAdapter(Context context, List<CheckWithProducts> list) {
        this.context = context;
        checkList = list;
        owner = (LifecycleOwner) context;
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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


        LiveData<List<Tag>> tags = ChecksRoller.getInstance().getAppDatabase()
                .getCheckDao().getTagsByCheckId(check.getId());
        layout = view.findViewById(R.id.check_tag_list);
        final LinearLayout linearLayout = layout;
        tags.observe(owner, tags1 -> updateTags(tags1, linearLayout));


        ((TextView) view.findViewById(R.id.name)).setText(check.getName());
        ((TextView) view.findViewById(R.id.sum)).setText("Total: "  +
                String.format("%.2f", check.getTotalSum() / 100.0));
        ((TextView) view.findViewById(R.id.data)).setText("Time: " + check.getDate().replace("T", " "));
        ((TextView) view.findViewById(R.id.shop)).setText("Shop: " + check.getShop());

        return view;
    }

    Check getCheck(int position) {
        return ((CheckWithProducts) getItem(position)).getCheck()   ;
    }

    private void updateTags(List<Tag> tags, LinearLayout layout1) {
        Adapter adapter = new TagAdapter(context, tags);
        layout1.removeAllViews();
        for (int i = 0; i < tags.size(); i++) {
            View child = adapter.getView(i, null, null);
            child.setBackgroundColor(tags.get(i).getColor());

            layout1.addView(child, TagListController.tagParams());
        }
        layout1.invalidate();
    }
}

