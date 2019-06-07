package com.project.spender.charts;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.project.spender.data.entities.Tag;
import com.project.spender.data.entities.TagWithSumAndDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class LineChartController extends UpdatableChartController<List<TagWithSumAndDate>> {

    private final LineChart lineChart;

    public LineChartController(LifecycleOwner owner, LineChart lineChart) {
        this.lineChart = lineChart;
        this.owner = owner;
        lineChartSetup();
    }

    private void lineChartSetup() {
        lineChart.getDescription().setEnabled(false);

        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);

        Legend legend = lineChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);

        ValueFormatter valueFormatter = new ValueFormatter() {

            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM", Locale.ROOT);

            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                long millis = TimeUnit.HOURS.toMillis((long)value);
                return mFormat.format(millis);
            }
        };

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGranularity(24f);
        xAxis.setValueFormatter(valueFormatter);
        xAxis.setDrawAxisLine(true);
        xAxis.setTextSize(10f);
        xAxis.setDrawGridLines(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        lineChart.getAxisRight().setEnabled(false);
    }

    /**
     * @param tagsWithSum список отсортированный по tag.id
     */
    @Override
    protected void setData(List<TagWithSumAndDate> tagsWithSum) {
        if (tagsWithSum.isEmpty()) {
            return;
        }

        List<ILineDataSet> dataSets = new ArrayList<>();

        List<Entry> entries = new ArrayList<>();


        TagWithSumAndDate endTag = new TagWithSumAndDate();
        tagsWithSum.add(endTag);

        Tag currentTag = tagsWithSum.get(0).tag;

        for (TagWithSumAndDate tagWithSumAndDate : tagsWithSum)
        {
            if (whiteIdList != null
                    && tagWithSumAndDate.tag.getId() != 0
                    && !whiteIdList.contains(tagWithSumAndDate.tag.getId())) {
                continue;
            }

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

            entries.add(new Entry(TimeUnit.MILLISECONDS.toHours(calendar.getTimeInMillis()), tagWithSumAndDate.sum/100f));
        }

        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.invalidate();
    }

    public void invalidate() {
        lineChart.invalidate();
    }
}
