package com.project.spender.activities;

import android.content.Context;
import android.graphics.Color;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.project.spender.ChecksRoller;
import com.project.spender.R;
import com.project.spender.TagStateHolder;
import com.project.spender.data.entities.Tag;

import java.util.Random;

import com.mattyork.colours.Colour;
import com.mattyork.colours.Colour.ColorScheme;

public class NewTagActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tag);

        Button enter = findViewById(R.id.enter_new_tag_info);
        TextInputEditText name = findViewById(R.id.new_tag_name);
        TextInputEditText color = findViewById(R.id.new_tag_color);
        TextInputEditText substring = findViewById(R.id.new_tag_substring);

        TagStateHolder tagStateHolder = new TagStateHolder();

        name.setOnEditorActionListener((textView, i, keyEvent) -> {
            tagStateHolder.setName(name.getText().toString());
            hideKeyboard(textView);
            return true;
        });

        color.setOnEditorActionListener((textView, i, keyEvent) -> {
            tagStateHolder.setName(color.getText().toString());
            hideKeyboard(textView);
            return true;
        });

        substring.setOnEditorActionListener((textView, i, keyEvent) -> {
            tagStateHolder.setRegEx(substring.getText().toString());
            hideKeyboard(textView);
            return true;
        });

        enter.setOnClickListener(v -> {
            tagStateHolder.createTag();
            finish();
        });
    }

    private void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}


