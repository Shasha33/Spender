package com.project.spender.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.project.spender.ChecksRoller;
import com.project.spender.R;
import com.project.spender.data.entities.Tag;

import java.util.List;

public class TagChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_choice);

        ListView list = findViewById(R.id.tag_list);

        List<Tag> tags = ChecksRoller.getInstance().getAppDatabase().getCheckDao().getAllTags();

        list.setOnItemClickListener((parent, view, position, id) -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("tag id", tags.get(position).getId());
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        list.setAdapter(new TagChoiceAdapter(this, tags));
    }
}
