package com.project.spender.data;

import android.app.Activity;
import android.util.Log;

import com.project.spender.roller.ChecksRoller;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for store result of code scanning
 */
public class ScanResult {

    //(todo) come up with natural clear name
    public static final int NOT_ENOUGH_DATA = -10;
    public static final int WRONG_CODE = -20;

    private final String fn;
    private final String fd;
    private final String fp;
    private final String date;
    private final String sum;

    private List<String> parseNumbers(String content) {
        List<String> res = new ArrayList<>();
        System.out.println(content);
        for (String i : content.split("[&|=|a-z]")) {
            if (i.length() != 0) {
                if (i.contains(".")) {
                    res.add(i.replace(".", ""));
                } else {
                    res.add(i);
                }
            }
        }
        return res;
    }

    /**
     * Returns text representation of given result code
     */
    public static String explain(int code) {
        switch (code) {
            case Activity.RESULT_OK:
                return "Scanned";
            case NOT_ENOUGH_DATA:
                return "Authorization required to continue";
            case WRONG_CODE:
                return "Content of the code does not match the format of checks codes";
            default:
                return "not scanned";
        }
    }

    public String getDate() {
        return date;
    }

    public String getFd() {
        return fd;
    }

    public String getFn() {
        return fn;
    }

    public String getFp() {
        return fp;
    }

    public String getSum() {
        return sum;
    }

    /**
     * Parses given string and creates new instance.
     * If string can not be parsed as check info throws exception
     */
    public ScanResult(String result) throws InvalidParameterException{
        List<String> resultNumbers = parseNumbers(result);
        for (String i : resultNumbers) {
            Log.i(ChecksRoller.LOG_TAG, i);
        }
        if (resultNumbers.size() < 5) {
            throw new InvalidParameterException();
        }
        fn = resultNumbers.get(2);
        fd = resultNumbers.get(3);
        fp = resultNumbers.get(4);
        date = resultNumbers.get(0);
        sum = resultNumbers.get(1);
    }

    public ScanResult(String fn, String fd, String fp, String date, String sum) {
        this.fn = fn;
        this.fd = fd;
        this.fp = fp;
        this.date = date;
        this.sum = sum;
    }
}
