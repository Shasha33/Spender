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

        int type = getIntent().getIntExtra("op type", -1);
        int pos = getIntent().getIntExtra("position", -2);

        List<Tag> tags = ChecksRoller.getInstance().getAppDatabase().getCheckDao().getAllTags();

        list.setOnItemClickListener((parent, view, position, id) -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("tag id", tags.get(position).getId());
            if (type != -1) {
                resultIntent.putExtra("position", pos);
            }
            if (type != -1) {
                resultIntent.putExtra("op type", type);
            }
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        list.setAdapter(new TagChoiceAdapter(this, tags));
    }
}
