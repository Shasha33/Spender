package com.project.spender.activities;

import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;

import com.project.spender.R;
import com.project.spender.TagStateHolder;

import static com.project.spender.charts.ChartsStateHolder.hideKeyboard;

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
}


