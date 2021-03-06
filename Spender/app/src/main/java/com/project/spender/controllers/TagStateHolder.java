package com.project.spender.controllers;

import android.graphics.Color;
import android.util.Log;

import androidx.annotation.Nullable;

import com.github.mikephil.charting.utils.ColorTemplate;
import com.project.spender.roller.App;
import com.project.spender.roller.ChecksRoller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

/**
 * Class for storage new tag and creating
 */
public class TagStateHolder {

    @Inject protected ChecksRoller checksRoller;
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

    public TagStateHolder() {
        App.getComponent().inject(this);
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Trying to parse color from given string.
     * If its possible saves it
     */
    public void setColor(String color) {
        Log.i(ChecksRoller.LOG_TAG, color + "");
        try {
            this.color = Color.parseColor(color);
        } catch (Exception e) {
            this.color = null;
        }
    }

    /**
     * Saves given string as regular expression for auto add to products
     */
    public void setRegEx(String regEx) {
        Log.i(ChecksRoller.LOG_TAG, "TRYING TO ADD NEW TAG " + regEx);
        if (!regEx.equals("")) {
            this.regEx = regEx;
        }
    }

    /**
     * Creates new tag with saved parameters
     * If where no saved color, uses random one
     */
    public void createTag() {
        if (color == null) {
            Random random = new Random();
            int index = random.nextInt(DEFAULT_COLOR.size());
            color = DEFAULT_COLOR.get(index);
        }

        checksRoller.addTag(name, regEx, color);
    }
}
