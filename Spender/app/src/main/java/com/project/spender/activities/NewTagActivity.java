package com.project.spender.activities;

import android.graphics.Color;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.project.spender.ChecksRoller;
import com.project.spender.R;
import com.project.spender.data.entities.Tag;

import java.util.List;
import java.util.Random;


public class NewTagActivity extends AppCompatActivity {

    private static final Integer[] DEFAULT_COLOR = {Color.RED, Color.GRAY, Color.GREEN, Color.BLACK,
    Color.BLUE, Color.CYAN, Color.YELLOW, Color.MAGENTA, Color.DKGRAY};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tag);

        Button enter = findViewById(R.id.enter_new_tag_info);
        TextInputEditText name = findViewById(R.id.new_tag_name);
        TextInputEditText color = findViewById(R.id.new_tag_color);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newTagColor;
                String newTagName = name.getText().toString();
                if (color.getText().toString().equals("")) {

                    Random random = new Random();
                    int index = random.nextInt(DEFAULT_COLOR.length);
                    newTagColor = DEFAULT_COLOR[index];
                } else {
                    newTagColor = Integer.parseInt(color.getText().toString());
                }

                ChecksRoller.getInstance().getAppDatabase().getCheckDao().insertTag(new Tag(newTagName, newTagColor));
                finish();
            }
        });
    }
}


