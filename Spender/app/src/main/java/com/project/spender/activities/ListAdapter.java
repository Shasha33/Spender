package com.project.spender.activities;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.project.spender.R;
import com.project.spender.data.entities.Check;
import com.project.spender.data.entities.CheckWithProducts;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<CheckWithProducts> checkList;

    ListAdapter(Context context, List<CheckWithProducts> list) {
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


        ((TextView) view.findViewById(R.id.name)).setText(check.getName());
        ((TextView) view.findViewById(R.id.sum)).setText("Total: "  +
                String.format("%.2f", check.getTotalSum() / 100.0));
        ((TextView) view.findViewById(R.id.shop)).setText("Shop: " + check.getShop());

        return view;
    }

    Check getCheck(int position) {
        return ((CheckWithProducts) getItem(position)).getCheck()   ;
    }

}

