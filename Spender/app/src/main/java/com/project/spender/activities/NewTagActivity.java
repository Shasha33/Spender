package com.project.spender.activities;

import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.project.spender.controllers.ChecksRoller;
import com.project.spender.R;
import com.project.spender.controllers.TagStateHolder;

import static com.project.spender.charts.ChartsStateHolder.hideKeyboard;

/**
 * Activity for adding new tag
 */
public class NewTagActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tag);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Button enter = findViewById(R.id.enter_new_tag_info);
        TextInputEditText name = findViewById(R.id.new_tag_name);
        TextInputEditText color = findViewById(R.id.new_tag_color);
        TextInputEditText substring = findViewById(R.id.new_tag_substring);

        TagStateHolder tagStateHolder = new TagStateHolder();

        name.setOnEditorActionListener((textView, i, keyEvent) -> {
            Log.i(ChecksRoller.LOG_TAG, name.getText().toString() + "");
            tagStateHolder.setName(name.getText().toString());
            hideKeyboard(textView);
            return true;
        });

        color.setOnEditorActionListener((textView, i, keyEvent) -> {
            tagStateHolder.setColor(color.getText().toString());
            hideKeyboard(textView);
            return true;
        });

        substring.setOnEditorActionListener((textView, i, keyEvent) -> {
            tagStateHolder.setRegEx(substring.getText().toString());
            hideKeyboard(textView);
            return true;
        });

        enter.setOnClickListener(v -> {
            tagStateHolder.setName(name.getText().toString());
            tagStateHolder.setColor(color.getText().toString());
            tagStateHolder.setRegEx(substring.getText().toString());
            tagStateHolder.createTag();
            setResult(Activity.RESULT_OK);
            finish();
        });
    }
}


