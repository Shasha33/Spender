package com.project.spender.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.spender.charts.PieChartController;
import com.project.spender.R;

import java.util.Set;


public class PieChartFragment extends ChartFragment {

    private PieChartController pieChartController;

    public PieChartFragment() {
        // Required empty public constructor
    }

    public static PieChartFragment newInstance() {
        return new PieChartFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pie_chart, container, false);

        //Pie
        pieChartController = new PieChartController(getViewLifecycleOwner(), view.findViewById(R.id.pieChart));
        pieChartController.setWhiteIdList(whiteIdList);
        resetData();

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void setWhiteIdList(Set<Long> whiteIdList) {
        super.setWhiteIdList(whiteIdList);
        if (pieChartController != null) {
            pieChartController.setWhiteIdList(whiteIdList);
        }
    }

    public void drawHole(boolean status) {
        pieChartController.drawHole(false);
        pieChartController.invalidate();
    }

    @Override
    public void resetData() {
        if (pieChartController != null) {
            pieChartController.setDataSource(checkDao.getTagsWithSumByData(leftDate, rightDate));
            pieChartController.animate();
        }
    }
}
