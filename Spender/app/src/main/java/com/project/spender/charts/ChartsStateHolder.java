package com.project.spender.charts;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.project.spender.DataHelper;

import java.security.InvalidParameterException;

public class ChartsStateHolder {

    private String beginDate;
    private String endDate;
    private long[] ids;

    public void setBeginDateInput(@NonNull EditText editText) {
        editText.setOnEditorActionListener((textView, i, keyEvent) -> {
            try {
                beginDate = DataHelper.dateConvert(textView.getText().toString());
            } catch (InvalidParameterException e) {
                beginDate = DataHelper.DEFAULT_BEGIN;
                return false;
            }
            hideKeyboard(textView);
            return true;
        });
    }

    public void setEndDateInput(@NonNull EditText editText) {
        editText.setOnEditorActionListener((textView, i, keyEvent) -> {
            try {
                endDate = DataHelper.dateConvert(textView.getText().toString());
            } catch (InvalidParameterException e) {
                endDate = DataHelper.DEFAULT_END;
                return false;
            }
            hideKeyboard(textView);
            return true;
        });
    }

    public void setIds(@NonNull long[] ids) {
        this.ids = ids;
    }

    public static void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    //(todo) your code here
}
