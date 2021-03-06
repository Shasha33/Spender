package com.project.spender.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.spender.R;
import com.project.spender.data.CheckStatus;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Adapter for check requests status list
 */
public class HistoryItemAdapter extends BaseAdapter {

    private LayoutInflater lInflater;
    private List<CheckStatus> list;

    public HistoryItemAdapter(Context context, List<CheckStatus> list) {
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

    private CheckStatus getCheckStatus(int position) {
        return (CheckStatus) getItem(position);
    }


}
