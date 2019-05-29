package com.project.spender.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import com.project.spender.ChecksRoller;
import com.project.spender.R;
import com.project.spender.charts.LineChartController;
import com.project.spender.data.CheckDao;
import com.project.spender.data.entities.TagWithSumAndDate;

public class LineChartFragment extends ChartFragment {

    private LineChartController lineChartController;

    public LineChartFragment() {
        // Required empty public constructor
    }

    public static LineChartFragment newInstance() {
        return new LineChartFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_line_chart, container, false);

        lineChartController = new LineChartController(getViewLifecycleOwner(), view.findViewById(R.id.lineChart));

        return view;
    }

    public LineChartController getLineChartController() {
        return lineChartController;
    }


    @Override
    public void resetData() {
        lineChartController.setDataSource(checkDao.getTagsWithSumAndDate());
    }
}
