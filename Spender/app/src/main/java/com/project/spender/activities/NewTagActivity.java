package com.project.spender.activities;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.project.spender.ChecksRoller;
import com.project.spender.R;
import com.project.spender.data.entities.Tag;

public class NewTagActivity extends AppCompatActivity {

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
                String newTagName = name.getText().toString();
                int newTagColor = Integer.parseInt(color.getText().toString());
                ChecksRoller.getInstance().getAppDatabase().getCheckDao().insertTag(new Tag(newTagName, newTagColor));
                Toast.makeText(NewTagActivity.this, "Added new tag", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
