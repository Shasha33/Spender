package com.project.spender.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.project.spender.R;
import com.project.spender.controllers.ChecksRoller;
import com.project.spender.controllers.TagListHelper;
import com.project.spender.data.CheckStatus;
import com.project.spender.data.entities.CheckWithProducts;
import com.project.spender.data.entities.Product;
import com.project.spender.data.entities.Tag;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class HistoryItemAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater lInflater;
    private List<CheckStatus> list;
    private LinearLayout layout;
    private LifecycleOwner owner;

    public HistoryItemAdapter(Context context, List<CheckStatus> list) {
        this.context = context;
        owner = (LifecycleOwner) context;
        this.list = list;
        lInflater = (LayoutInflater) context
                .getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = lInflater.inflate(R.layout.history_item, parent, false);
        }

        final View view = convertView;

        CheckStatus item = getCheckStatus(position);


        ((TextView) view.findViewById(R.id.name)).setText(item.getStatus());
        ((TextView) view.findViewById(R.id.time)).setText("Requested : " + item.getTime() );

        return view;
    }

    CheckStatus getCheckStatus(int position) {
        return (CheckStatus) getItem(position);
    }


}
