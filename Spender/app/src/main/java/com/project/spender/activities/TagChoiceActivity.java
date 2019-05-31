package com.project.spender.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import com.project.spender.R;
import com.project.spender.controllers.TagChoiceController;

public class TagChoiceActivity extends AppCompatActivity implements LifecycleOwner {

    private TagChoiceController controller;
    private ListView listView;
    private Button enter;
    private LifecycleRegistry lifecycleRegistry;

    @Override
    public void onStart() {
        super.onStart();
        lifecycleRegistry.markState(Lifecycle.State.STARTED);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tag_choice_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_tag:
                startActivity(new Intent(this, NewTagActivity.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_choice);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        lifecycleRegistry = new LifecycleRegistry(this);
        lifecycleRegistry.markState(Lifecycle.State.CREATED);

        listView = findViewById(R.id.tag_list);
        controller = new TagChoiceController(this, listView);

        enter = findViewById(R.id.enter_tag_set);
        enter.setOnClickListener(view -> {
            setResult(Activity.RESULT_OK, controller.resultIntent());
            finish();
        });
    }
}
