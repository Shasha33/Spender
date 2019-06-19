package com.project.spender.controllers;

import android.util.Log;

import com.project.spender.roller.ChecksRoller;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Class for generalizing dates usage
 */
public abstract class DateHelper {

    public static final String DEFAULT_BEGIN = "1999-03-22";
    public static final String DEFAULT_END = "now";

    /**
     * Converts date from DD.MM.YYYY to YYYY-MM-DD
     */
    public static String dateConvert(String date) throws IllegalArgumentException, ParseException {
        SimpleDateFormat formatInput = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat formatResult = new SimpleDateFormat("yyyy-MM-dd");

        Log.i(ChecksRoller.LOG_TAG, formatResult.format(formatInput.parse(date)));
        return formatResult.format(formatInput.parse(date));


    }
}
