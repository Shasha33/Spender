package com.project.spender.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.project.spender.R;
import com.project.spender.controllers.ChecksRoller;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Button cat = findViewById(R.id.misha_kot);
        cat.setOnClickListener(view -> ChecksRoller.getInstance().setCatMode());
    }
}
