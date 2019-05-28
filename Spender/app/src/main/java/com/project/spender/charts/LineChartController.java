package com.project.spender.charts;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.project.spender.data.entities.Tag;
import com.project.spender.data.entities.TagWithSumAndDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LineChartController {
    private final LineChart lineChart;

    private LiveData<List<TagWithSumAndDate>> dataSource;
    private Observer<List<TagWithSumAndDate>> observer = this::setData;
    private LifecycleOwner owner;

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

        List<Entry> entries = new ArrayList<>();


        TagWithSumAndDate endTag = new TagWithSumAndDate();
        tagsWithSum.add(endTag);

        Tag currentTag = tagsWithSum.get(0).tag;

        for (TagWithSumAndDate tagWithSumAndDate : tagsWithSum)
        {
            if (currentTag.getId() != tagWithSumAndDate.tag.getId()) {

                LineDataSet data = new LineDataSet(entries, currentTag.getName());
                data.setCircleColor(currentTag.getColor());
                data.setColor(currentTag.getColor());
                dataSets.add(data);
                entries = new ArrayList<>();
                currentTag = tagWithSumAndDate.tag;
            }

            if (tagWithSumAndDate.tag.getId() == 0) {
                continue;
            }

            Calendar calendar = Calendar.getInstance();
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss", Locale.ROOT);
                calendar.setTime(format.parse(tagWithSumAndDate.date));
            } catch (ParseException e) {
                Log.wtf("PARSER", "Cannot parse date from string. Exception: " + e.getMessage());
            }

            entries.add(new Entry(tagWithSumAndDate.sum, calendar.getTimeInMillis()));
        }

        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.invalidate();
    }
}
