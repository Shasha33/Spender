package com.project.spender.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import com.project.spender.controllers.ChecksRoller;
import com.project.spender.R;
import com.project.spender.controllers.CheckShowHelper;

import static com.project.spender.controllers.TagChoiceHelper.TAG_ID_LIST;

public class CheckShowActivity extends AppCompatActivity implements LifecycleOwner {

    private ListView listView;
    private EditText search;
    private CheckShowHelper controller;
    private LifecycleRegistry lifecycleRegistry;

    @Override
    public void onStart() {
        super.onStart();
        lifecycleRegistry.markState(Lifecycle.State.STARTED);
    }


    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }

    private static final int ADDING_CODE = 20;
    private static final int REMOVING_CODE = 40;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }

        long[] tags = data.getLongArrayExtra(TAG_ID_LIST);
        if (tags == null) {
            return;
        }

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ADDING_CODE) {
                controller.addTags(tags);
            } else if (requestCode == REMOVING_CODE) {
                controller.removeTags(tags);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.products_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.remove:
                controller.removeProducts();
                return true;

            case R.id.add_tag:
                Intent intentTagChoice = new Intent(this, TagChoiceActivity.class);
                startActivityForResult(intentTagChoice, ADDING_CODE);
                return true;
            case R.id.remove_tag:
                intentTagChoice = new Intent(this, TagChoiceActivity.class);
                startActivityForResult(intentTagChoice, REMOVING_CODE);
                return true;

            default:

                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_show);


        lifecycleRegistry = new LifecycleRegistry(this);
        lifecycleRegistry.markState(Lifecycle.State.CREATED);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        listView = findViewById(R.id.productsList);
        search = findViewById(R.id.search_in_check);

        controller = new CheckShowHelper(this, listView, getIntent());
        controller.setInput(search);
    }

}
