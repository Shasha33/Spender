package com.project.spender.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.spender.ChecksRoller;
import com.project.spender.R;
import com.project.spender.data.entities.Check;
import com.project.spender.data.entities.CheckWithProducts;
import com.project.spender.data.entities.Tag;

import java.util.List;

public class ListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<CheckWithProducts> checkList;

    public ListAdapter(Context context, List<CheckWithProducts> list) {
        this.context = context;
        checkList = list;
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


        List<Tag> tags = ChecksRoller.getInstance().getAppDatabase()
                .getCheckDao().getTagsByCheckId(check.getId());
        LinearLayout layout = view.findViewById(R.id.check_tag_list);
        Adapter adapter = new TagAdapter(context, tags);
        layout.removeAllViews();
        for (int i = 0; i < tags.size(); i++) {
            View child = adapter.getView(i, null, null);
            child.setBackgroundColor(tags.get(i).getColor());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(50, 30);
            params.setMargins(5, 0, 5, 0);
            layout.addView(child, params);
        }


        ((TextView) view.findViewById(R.id.name)).setText(check.getName() + " from " + check.getShop());
        ((TextView) view.findViewById(R.id.sum)).setText("Total: "  +
                String.format("%.2f", check.getTotalSum() / 100.0));
        ((TextView) view.findViewById(R.id.data)).setText("Time: " + check.getDate());

        return view;
    }

    Check getCheck(int position) {
        return ((CheckWithProducts) getItem(position)).getCheck()   ;
    }

}

