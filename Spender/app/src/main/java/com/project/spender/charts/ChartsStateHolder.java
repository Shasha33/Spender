package com.project.spender.charts;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.project.spender.DataHelper;
import com.project.spender.fragments.ChartFragment;

import java.util.HashSet;
import java.util.Set;

public class ChartsStateHolder {

    private String beginDate = DataHelper.DEFAULT_BEGIN;
    private String endDate = DataHelper.DEFAULT_END;
    private long[] ids;

    private ChartFragment chartFragment;

    public void setBeginDateInput(@NonNull EditText editText) {
        editText.setOnEditorActionListener((textView, i, keyEvent) -> {
            try {
                beginDate = DataHelper.dateConvert(textView.getText().toString());
            } catch (Exception e) {
                beginDate = DataHelper.DEFAULT_BEGIN;
            }
            hideKeyboard(textView);
            updateFragment();
            return true;
        });
    }

    public void setEndDateInput(@NonNull EditText editText) {
        editText.setOnEditorActionListener((textView, i, keyEvent) -> {
            try {
                endDate = DataHelper.dateConvert(textView.getText().toString());
            } catch (Exception e) {
                endDate = DataHelper.DEFAULT_END;
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
