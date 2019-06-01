package com.project.spender.charts;

import android.graphics.Color;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.StackedValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.project.spender.data.entities.TagWithSum;
import com.project.spender.data.entities.TagWithSumAndDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StackedBarChartController {
    private final BarChart barChart;

    private LiveData<List<TagWithSumAndDate>> dataSource;
    private Observer<List<TagWithSumAndDate>> observer = this::setData;
    private LifecycleOwner owner;

    private final int speed = 1400;

    private Set<Long> whiteIdList;

    public StackedBarChartController(LifecycleOwner owner, BarChart barChart) {
        this.barChart = barChart;
        this.owner = owner;
        StackedBarChartSetup();
    }

    private void StackedBarChartSetup() {
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.setMaxVisibleValueCount(60);
        barChart.setPinchZoom(false);

        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);

        barChart.setDrawValueAboveBar(false);
        barChart.setHighlightFullBarEnabled(false);
        barChart.getAxisRight().setEnabled(false);

        Legend legend = barChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
//        legend.setFormSize(8f);
//        legend.setFormToTextSpace(4f);
//        legend.setXEntrySpace(6f);

    }

    public void setDataSource(LiveData<List<TagWithSumAndDate>> data) {
        if (dataSource != null) {
            dataSource.removeObserver(observer);
        }
        dataSource = data;
        dataSource.observe(owner, observer);
    }

    private void setData(List<TagWithSumAndDate> data) {
//        List<Integer> colors = new ArrayList<>();
//
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0;;) {
            while(entries : )
        }
//        BarDataSet set1;
//        if (barChart.getData() != null &&
//                barChart.getData().getDataSetCount() > 0) {
//            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
//            set1.setValues(values);
//            barChart.getData().notifyDataChanged();
//            barChart.notifyDataSetChanged();
//        } else {
//            set1 = new BarDataSet(values, "Statistics Vienna 2014");
//            set1.setDrawIcons(false);
//            set1.setColors(getColors());
//            set1.setStackLabels(new String[]{"Births", "Divorces", "Marriages"});
//
//            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
//            dataSets.add(set1);
//
//            BarData data = new BarData(dataSets);
//            data.setValueFormatter(new StackedValueFormatter(false, "", 1));
//            data.setValueTextColor(Color.WHITE);
//
//            barChart.setData(data);
//        }
//        this.barChart.invalidate();
    }

    public void animate() {
        barChart.animateXY(speed, speed);
    }


    public void invalidate() {
        barChart.invalidate();
    }

    public void setWhiteIdList(Set<Long> whiteIdList) {
        this.whiteIdList = whiteIdList;
    }
}
