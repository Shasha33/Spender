package com.project.spender;

import android.util.Log;

import androidx.annotation.Nullable;

import com.mattyork.colours.Colour;
import com.project.spender.data.entities.Check;
import com.project.spender.data.entities.Tag;

import java.util.Random;

public class TagStateHolder {

    private static final int[] DEFAULT_COLOR = {Colour.watermelonColor(),
            Colour.maroonColor(), Colour.waveColor(), Colour.goldenrodColor(), Colour.grapeColor(),
            Colour.linenColor(), Colour.mandarinColor(), Colour.coffeeColor(), Colour.crimsonColor(),
            Colour.pinkLipstickColor(), Colour.hollyGreenColor(), Colour.chiliPowderColor(),
            Colour.indigoColor(), Colour.raspberryColor(), Colour.orchidColor(), Colour.buttermilkColor()};

    private String name;
    @Nullable private String regEx;
    private Integer color;

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        try {
            this.color = Integer.parseInt(color);
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
            int index = random.nextInt(DEFAULT_COLOR.length);
            color = DEFAULT_COLOR[index];
        }

        ChecksRoller.getInstance().getAppDatabase()
                .getCheckDao().insertTag(new Tag(name, color, regEx));
    }
}
