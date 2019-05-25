package com.project.spender.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.project.spender.ChecksRoller;
import com.project.spender.R;
import com.project.spender.data.entities.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_choice);

        ListView list = findViewById(R.id.tag_list);

        List<Tag> tags = ChecksRoller.getInstance().getAppDatabase().getCheckDao().getAllTags();

        List<String> tagNames = new ArrayList<>();
        for (Tag tag : tags) {
            tagNames.add(tag.getName());
        }

        list.setOnItemClickListener((parent, view, position, id) -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("tag id", tags.get(position).getId());
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        list.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_single_choice, tagNames));
    }
}
