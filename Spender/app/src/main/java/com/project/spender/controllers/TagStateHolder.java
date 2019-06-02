package com.project.spender.controllers;

import android.graphics.Color;
import android.util.Log;

import androidx.annotation.Nullable;

import com.github.mikephil.charting.utils.ColorTemplate;
import com.project.spender.data.entities.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TagStateHolder {

    private static final List<Integer> DEFAULT_COLOR = new ArrayList<>();

    static {
        for (int i : ColorTemplate.VORDIPLOM_COLORS) {
            DEFAULT_COLOR.add(i);
        }

        for (int i : ColorTemplate.MATERIAL_COLORS) {
            DEFAULT_COLOR.add(i);
        }

        for (int i : ColorTemplate.JOYFUL_COLORS) {
            DEFAULT_COLOR.add(i);
        }

        for (int i : ColorTemplate.LIBERTY_COLORS) {
            DEFAULT_COLOR.add(i);
        }

        for (int i : ColorTemplate.COLORFUL_COLORS) {
            DEFAULT_COLOR.add(i);
        }

        for (int i : ColorTemplate.PASTEL_COLORS) {
            DEFAULT_COLOR.add(i);
        }


    }

    private String name;
    @Nullable private String regEx;
    private Integer color;

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        Log.i(ChecksRoller.LOG_TAG, color + "");
        try {
            this.color = Color.parseColor(color);
        } catch (Exception e) {
            this.color = null;
        }
    }

    public void setRegEx(String regEx) {
        Log.i(ChecksRoller.LOG_TAG, "TRYING TO ADD NEW TAF " + regEx);
        this.regEx = regEx;
    }

    public void createTag() {
        if (color == null) {
            Random random = new Random();
            int index = random.nextInt(DEFAULT_COLOR.size());
            color = DEFAULT_COLOR.get(index);
        }

        ChecksRoller.getInstance().getAppDatabase()
                .getCheckDao().insertTag(new Tag(name, color, regEx));
    }
}
