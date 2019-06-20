package com.project.spender.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.project.spender.charts.PieChartController;
import com.project.spender.R;
import com.project.spender.roller.App;
import com.project.spender.roller.ChecksRoller;

import java.util.Set;

import javax.inject.Inject;


public class PieChartFragment extends ChartFragment {

    @Inject protected ChecksRoller checksRoller;
    private ImageButton secret;
    private int clickCounter;
    private final static int MAGIC_CONST = 30;

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
        App.getComponent().inject(this);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pie_chart, container, false);

        secret = view.findViewById(R.id.secret);
        secret.setOnClickListener(v -> {
            clickCounter++;
            if (clickCounter > MAGIC_CONST) {
                secret.setImageResource(R.drawable.clevercat);
//                clickCounter = 0;
            } else {
                secret.setImageResource(R.drawable.cat);
            }

            checksRoller.setCatMode();
        });

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
        if (status) {
            secret.setVisibility(View.VISIBLE);
        } else {
            secret.setVisibility(View.GONE);
        }
        pieChartController.drawHole(status);
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
