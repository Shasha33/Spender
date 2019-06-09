package com.project.spender.charts;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.project.spender.controllers.DateHelper;
import com.project.spender.fragments.ChartFragment;

import java.util.HashSet;
import java.util.Set;

/**
 * Class to store time period and list of tags for chart and its fragment.
 */
public class ChartsStateHolder {

    private String beginDate = DateHelper.DEFAULT_BEGIN;
    private String endDate = DateHelper.DEFAULT_END;
    private long[] ids;

    private ChartFragment chartFragment;

    public void setBeginDateInput(Context context, @NonNull EditText editText) {
        editText.setOnEditorActionListener((textView, i, keyEvent) -> {
            try {
                beginDate = DateHelper.dateConvert(textView.getText().toString());
            } catch (Exception e) {
                beginDate = DateHelper.DEFAULT_BEGIN;
                Toast.makeText(context, "Invalid data format", Toast.LENGTH_SHORT).show();
            }
            hideKeyboard(textView);
            updateFragment();
            return true;
        });
    }

    public void setEndDateInput(Context context, @NonNull EditText editText) {
        editText.setOnEditorActionListener((textView, i, keyEvent) -> {
            try {
                endDate = DateHelper.dateConvert(textView.getText().toString());
            } catch (Exception e) {
                endDate = DateHelper.DEFAULT_END;
                Toast.makeText(context, "Invalid data format", Toast.LENGTH_SHORT).show();
            }
            hideKeyboard(textView);
            updateFragment();
            return true;
        });
    }

    public void setIds(@NonNull long[] ids) {
        this.ids = ids;
        updateFragment();
    }

    public static void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public void setChartFragment(ChartFragment chartFragment) {
        this.chartFragment = chartFragment;
        updateFragment();
    }

    public void updateFragment() {
        chartFragment.setPeriod(beginDate, endDate);
        Set<Long> whiteList = null;
        if (ids != null) {
            whiteList = new HashSet<>(ids.length);
            for (long id : ids) {
                whiteList.add(id);
            }
        }
        chartFragment.setWhiteIdList(whiteList);
        chartFragment.resetData();
    }
}
