package com.project.spender.data;

import android.app.Activity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ScanResult {

    //(todo) come up with natural clear name
    public static final int NOT_ENOUGH_DATA = -10;

    private final String fn;
    private final String fd;
    private final String fp;
    private final String date;
    private final String sum;

    public List<String> parseNumbers(String content) {
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

    public static String explain(int code) {
        switch (code) {
            case Activity.RESULT_OK:
                return "Scanned";
            case NOT_ENOUGH_DATA:
                return "Authorization required to continue";
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

    public ScanResult(String result) {
        List<String> resultNumbers = parseNumbers(result);
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
