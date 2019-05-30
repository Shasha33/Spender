package com.project.spender;

import android.util.Log;

import com.project.spender.data.entities.Check;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class DataHelper {


    public static final String DEFAULT_BEGIN = "1999-03-22";
    public static final String DEFAULT_END = "now";

    /**
     * Converts date from DD.MM.YYYY to YYYY-MM-DD
     */
    public static String dateConvert(String date) throws IllegalArgumentException {
        SimpleDateFormat formatInput = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat formatResult = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Log.i(ChecksRoller.LOG_TAG, formatResult.format(formatInput.parse(date)));
            return formatResult.format(formatInput.parse(date));
        } catch (ParseException e) {
            return null;
        }

    }
}
