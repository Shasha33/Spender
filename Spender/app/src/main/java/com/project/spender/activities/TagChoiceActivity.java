package com.project.spender.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.project.spender.ChecksRoller;
import com.project.spender.R;
import com.project.spender.data.entities.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagChoiceActivity extends AppCompatActivity {

    private List<Tag> clickedTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_choice);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        clickedTags = new ArrayList<>();

        ListView list = findViewById(R.id.tag_list);

        int type = getIntent().getIntExtra("op type", -1);
        int pos = getIntent().getIntExtra("position", -2);

        List<Tag> tags = ChecksRoller.getInstance().getAppDatabase().getCheckDao().getAllTags();

        list.setOnItemClickListener((parent, view, position, id) -> {
            Tag current = tags.get(position);
            if (clickedTags.contains(current)) {
                clickedTags.remove(current);
                view.setBackgroundColor(Color.WHITE);
            } else {
                clickedTags.add(current);
                view.setBackgroundColor(CheckShowActivity.SELECTED_ITEM);
            }
        });

        list.setAdapter(new TagChoiceAdapter(this, tags));

        Button enter = findViewById(R.id.enter_tag_set);
        enter.setOnClickListener(view -> {
            Intent resultIntent = new Intent();
            int size = clickedTags.size();
            long[] ids = new long[size];
            for (int i = 0; i < size; i++) {
                ids[i] = clickedTags.get(i).getId();
            }
            resultIntent.putExtra("tag ids", ids);
            if (type != -1) {
                resultIntent.putExtra("position", pos);
            }
            if (type != -1) {
                resultIntent.putExtra("op type", type);
            }
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
