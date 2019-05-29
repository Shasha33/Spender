package com.project.spender.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.spender.R;
import com.project.spender.charts.StackedBarChartController;

public class StackedBarChartFragment extends ChartFragment {

    StackedBarChartController stackedBarChartController;

    public StackedBarChartFragment() {
        // Required empty public constructor
    }

    public static StackedBarChartFragment newInstance() {
        return new StackedBarChartFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stacked_bar_chart, container, false);

        return view;
    }

    @Override
    public void resetData() {

    }
}
