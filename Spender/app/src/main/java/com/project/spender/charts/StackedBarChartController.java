package com.project.spender.charts;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.StackedValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.project.spender.data.entities.TagWithSumAndDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class StackedBarChartController extends UpdatableChartController<List<TagWithSumAndDate>> {
    private final BarChart barChart;

    private final int speed = 1400;

    private List<Calendar> dates;

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

        ValueFormatter valueFormatter = new ValueFormatter() {

            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM", Locale.ROOT);

            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                int i = (int) value;
                return mFormat.format(dates.get(i).getTime());
            }
        };

        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(valueFormatter);

        Legend legend = barChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
    }


    private Calendar getTagDate(TagWithSumAndDate tagWSD) throws ParseException {
        Calendar tagTime = Calendar.getInstance();
        tagTime.setTime(format.parse(tagWSD.date));
        return tagTime;
    }

    /**
     * @param data отсортирован по дате
     */
    @Override
    protected void setData(List<TagWithSumAndDate> data) {
        if (data.isEmpty()) {
            return;
        }

        List<IBarDataSet> dataSets = new ArrayList<>();
        dates = new ArrayList<>();

        int num = 0;
        for (ListIterator<TagWithSumAndDate> iter = data.listIterator(); iter.hasNext();) {
            Calendar time = Calendar.getInstance();
            TagWithSumAndDate currTagWSD = iter.next();
            try {
                time = getTagDate(currTagWSD);
            } catch (ParseException e) {
                Log.e("PARSER", "Parser error", e);
            }
            dates.add(time);
            List<Float> values = new ArrayList<>();
            List<Integer> colors = new ArrayList<>();
            values.add(currTagWSD.sum/100f);
            colors.add(currTagWSD.tag.getColor());
            while (iter.hasNext()) {
                currTagWSD = iter.next();
                try {
                    if (getTagDate(currTagWSD).getTimeInMillis() != time.getTimeInMillis()) {
                        iter.previous();
                        break;
                    }
                } catch (ParseException e) {
                    Log.e("PARSER", "Parser error", e);
                }
                values.add(currTagWSD.sum/100f);
                colors.add(currTagWSD.tag.getColor());
            }

            float[] arrValues = new float[values.size()];
            int i = 0;
            for (Float v : values) {
                arrValues[i++] = v;
            }
            BarEntry barEntry = new BarEntry(num++, arrValues);
            BarDataSet dataSet = new BarDataSet(Collections.singletonList(barEntry), data.toString());
            dataSet.setColors(colors);
            dataSets.add(dataSet);
        }
        BarData barData = new BarData(dataSets);
        barData.setValueFormatter(new StackedValueFormatter(false,"",1));
        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.invalidate();
    }

    public void animate() {
        barChart.animateXY(speed, speed);
    }


    public void invalidate() {
        barChart.invalidate();
    }
}
