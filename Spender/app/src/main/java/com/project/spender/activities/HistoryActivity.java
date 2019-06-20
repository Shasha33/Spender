package com.project.spender.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.project.spender.R;
import com.project.spender.roller.App;
import com.project.spender.roller.ChecksRoller;

import javax.inject.Inject;

/**
 * Activity for showing list of check requests and its statuses in current session
 */
public class HistoryActivity extends AppCompatActivity {

    @Inject ChecksRoller checksRoller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getComponent().inject(this);
        setContentView(R.layout.activity_history);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        TextView textView = findViewById(R.id.info_about_history);
        ListView listView = findViewById(R.id.history_list);
        checksRoller.getHistoryListHolder().setListView(this, listView, textView);
    }
}
