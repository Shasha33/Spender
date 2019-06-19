package com.project.spender.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.project.spender.R;
import com.project.spender.activities.TagChoiceActivity;
import com.project.spender.controllers.ChecksRoller;
import com.project.spender.controllers.HistoryListHolder;
import com.project.spender.data.entities.CheckWithProducts;

/**
 * Activity for showing list of check requests and its statuses in current session
 */
public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TextView textView = findViewById(R.id.info_about_history);
        ListView listView = findViewById(R.id.history_list);
        ChecksRoller.getInstance().getHistoryListHolder().setListView(this, listView, textView);
    }
}
