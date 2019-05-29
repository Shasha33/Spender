package com.project.spender.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.project.spender.ChecksRoller;
import com.project.spender.R;
import com.project.spender.charts.LineChartController;
import com.project.spender.data.CheckDao;

public class LineChartFragment extends Fragment {

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
        CheckDao checkDao = ChecksRoller.getInstance().getAppDatabase().getCheckDao();
        lineChartController.setDataSource(checkDao.getTagsWithSumAndDate());

        return view;
    }

    public LineChartController getLineChartController() {
        return lineChartController;
    }
}
