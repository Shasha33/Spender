package com.project.spender;

import android.graphics.Color;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.project.spender.data.entities.TagWithSum;

import java.util.ArrayList;
import java.util.List;

public class PieChartController {
    private final PieChart pieChart;

    private final int speed = 1400;

    public PieChartController(PieChart pieChart) {
        this.pieChart = pieChart;
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setEntryLabelTextSize(18f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.getLegend().setEnabled(false);
    }

    public void setData(List<TagWithSum> tagsWithSum) {
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
}
