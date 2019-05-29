package com.project.spender.charts;

import android.graphics.Color;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.project.spender.data.entities.TagWithSum;

import java.util.ArrayList;
import java.util.List;

public class PieChartController {
    private final PieChart pieChart;

    private LiveData<List<TagWithSum>> dataSource;
    private Observer<List<TagWithSum>> observer = this::setData;
    private LifecycleOwner owner;

    private final int speed = 1400;

    public PieChartController(LifecycleOwner owner, PieChart pieChart) {
        this.pieChart = pieChart;
        this.owner = owner;
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setEntryLabelTextSize(18f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.getLegend().setEnabled(false);
    }

    public void setDataSource(LiveData<List<TagWithSum>> data) {
        if (dataSource != null) {
            dataSource.removeObserver(observer);
        }
        dataSource = data;
        dataSource.observe(owner, observer);
    }

    private void setData(List<TagWithSum> tagsWithSum) {
        List<PieEntry> entries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        for (TagWithSum tws : tagsWithSum) {
            entries.add(new PieEntry(tws.sum, tws.tag.getName()));
            colors.add(tws.tag.getColor());
        }

        PieDataSet dataSet = new PieDataSet(entries, "Tags");
        dataSet.setColors(colors);
//        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        dataSet.setSliceSpace(3f);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(20f);
        data.setValueFormatter(new PercentFormatter(pieChart));

        pieChart.setData(data);
        pieChart.invalidate();
    }

    public void animate() {
        pieChart.animateXY(speed, speed);
    }

    public void drawHole(boolean enable) {
        pieChart.setDrawHoleEnabled(enable);
    }

    public void invalidate() {
        pieChart.invalidate();
    }
}
