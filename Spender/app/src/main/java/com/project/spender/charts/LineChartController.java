package com.project.spender.charts;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.project.spender.data.entities.TagWithSumAndDate;

import java.util.ArrayList;
import java.util.List;

public class LineChartController {
    private final LineChart lineChart;

    private LiveData<List<TagWithSumAndDate>> dataSource;
    private Observer<List<TagWithSumAndDate>> observer = this::setData;
    private LifecycleOwner owner;

    private final int speed = 1400;

    public LineChartController(LifecycleOwner owner, LineChart lineChart) {
        this.lineChart = lineChart;
        this.owner = owner;
        lineChartSetup();
    }

    private void lineChartSetup() {
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);

        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);

        Legend legend = lineChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
    }

    public void setDataSource(LiveData<List<TagWithSumAndDate>> data) {
        if (dataSource != null) {
            dataSource.removeObserver(observer);
        }
        dataSource = data;
        dataSource.observe(owner, observer);
    }

    private void setData(List<TagWithSumAndDate> tagsWithSum) {
        List<ILineDataSet> dataSets = new ArrayList<>();

//        for (TagWithSumAndDate twsd : tagsWithSum) {
//            List<Entry> entries = new ArrayList<>();
//            entries.add(new Entry(tws.sum, tws.tag.getName()));
//            colors.add(tws.tag.getColor());
//        }
//
//        PieDataSet dataSet = new PieDataSet(entries, "Tags");
//        dataSet.setColors(colors);
////        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
//        dataSet.setSliceSpace(3f);
//
//        PieData data = new PieData(dataSet);
//        data.setValueTextSize(20f);
//        data.setValueFormatter(new PercentFormatter(lineChart));
//
//        lineChart.setData(data);
//        lineChart.invalidate();
    }

    public void animate() {
        lineChart.animateXY(speed, speed);
    }
}
