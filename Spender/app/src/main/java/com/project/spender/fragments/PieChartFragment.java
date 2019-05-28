package com.project.spender.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.spender.charts.PieChartController;
import com.project.spender.ChecksRoller;
import com.project.spender.R;
import com.project.spender.data.CheckDao;


public class PieChartFragment extends Fragment {

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
        CheckDao checkDao = ChecksRoller.getInstance().getAppDatabase().getCheckDao();
        pieChartController.setDataSource(checkDao.getTagsWithSum());
        pieChartController.animate();

        // Inflate the layout for this fragment
        return view;
    }

    public PieChartController getPieChartController() {
        return pieChartController;
    }
}
