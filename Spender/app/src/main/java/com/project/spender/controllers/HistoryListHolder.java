package com.project.spender.controllers;

import android.content.Context;
import android.widget.Adapter;
import android.widget.ListView;

import androidx.lifecycle.LiveData;

import com.project.spender.adapters.HistoryItemAdapter;
import com.project.spender.data.CheckStatus;
import com.project.spender.data.entities.CheckWithProducts;

import java.util.ArrayList;
import java.util.List;

public class HistoryListHolder {

    private ListView listView;
    private List<CheckStatus> list;

    public HistoryListHolder() {
        list = new ArrayList<>();
    }

    public void setListView(Context context, ListView listView) {
        this.listView = listView;
        listView.setAdapter(new HistoryItemAdapter(context, list));
    }

    public void add(CheckStatus checkStatus) {
        list.add(checkStatus);
        upgrade();
    }

    public void update() {
        if (listView != null) {
            listView.invalidateViews();
        }
    }

    public void upgrade() {
        if (listView != null) {
            listView.invalidate();
        }
    }
}
