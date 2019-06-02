package com.project.spender.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import com.project.spender.controllers.CheckListHolder;
import com.project.spender.controllers.ChecksRoller;
import com.project.spender.R;
import com.project.spender.data.ScanResult;
import com.project.spender.data.entities.CheckWithProducts;

import static com.project.spender.charts.ChartsStateHolder.hideKeyboard;
import static com.project.spender.controllers.TagChoiceHelper.TAG_ID_LIST;


public class ListActivity extends AppCompatActivity implements LifecycleOwner {

    private ListView listView;
    private EditText request;

    private LifecycleRegistry lifecycleRegistry;

    private CheckListHolder holder;

    private ImageButton scan;
    private ImageButton list;
    private ImageButton statistics;
    private EditText beginDate;
    private EditText endDate;

    private static final int CHOOSE_TAG_FOR_ADD = 123;
    private static final int CHOOSE_TAG_FOR_REMOVE = 124;
    private static final int CHOOSE_TAG_FOR_SHOW = 125;
    private static final int SCAN_CODE = 42;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SCAN_CODE) {
            Toast.makeText(this, ScanResult.explain(resultCode), Toast.LENGTH_SHORT).show();
        }


        if (data == null) {
            return;
        }

        if (resultCode == Activity.RESULT_OK) {

            long[] tagIds = data.getLongArrayExtra(TAG_ID_LIST);

            if (requestCode == CHOOSE_TAG_FOR_SHOW) {
                holder.setTags(tagIds);
            } else if (requestCode == CHOOSE_TAG_FOR_ADD) {
                holder.addTagsForItem(tagIds);
            } else if (requestCode == CHOOSE_TAG_FOR_REMOVE) {
                holder.removeTagsForItem(tagIds);
            }
        }
    }

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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.list_view_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.add_tag_for_check:
                Intent intent = new Intent(this, TagChoiceActivity.class);
                holder.chooseItem(info.position);
                startActivityForResult(intent, CHOOSE_TAG_FOR_ADD);
                break;

            case R.id.remove_tag_for_check:
                intent = new Intent(this, TagChoiceActivity.class);
                holder.chooseItem(info.position);
                startActivityForResult(intent, CHOOSE_TAG_FOR_REMOVE);
                break;

            case R.id.remove_check:
                holder.removeItem(info.position);
                break;

            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_view_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_switch:
                holder.changeMode();
                item.setChecked(holder.getMode());
                if (holder.getMode()) {
                    beginDate.setVisibility(View.GONE);
                    endDate.setVisibility(View.GONE);
                } else {
                    beginDate.setVisibility(View.VISIBLE);
                    endDate.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.set_tags_for_list:
                Intent intent = new Intent(this, TagChoiceActivity.class);
                startActivityForResult(intent, CHOOSE_TAG_FOR_SHOW);
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_list);

        TextView info = findViewById(R.id.info_about_list);

        lifecycleRegistry = new LifecycleRegistry(this);
        lifecycleRegistry.markState(Lifecycle.State.CREATED);


        scan = findViewById(R.id.scan);
        statistics = findViewById(R.id.statistics);
        list = findViewById(R.id.list);
        list.setImageResource(R.drawable.history_chosen);

        statistics.setOnClickListener(v -> {
            finish();
        });

        scan.setOnClickListener(v -> {
            final Intent intent = new Intent(ListActivity.this, ScanActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivityForResult(intent, SCAN_CODE);
        });

        listView = findViewById(R.id.productsList);
        registerForContextMenu(listView);

        holder = new CheckListHolder(listView, this, info);

        request = findViewById(R.id.request);
        request.setOnEditorActionListener((v, actionId, event) -> {

            holder.setSubstring(request.getText().toString());

            hideKeyboard(v);
            return true;
        });

        beginDate = findViewById(R.id.begin_date);
        beginDate.setOnEditorActionListener((v, actionId, event) -> {

            Log.i(ChecksRoller.LOG_TAG, "Got begin value " + beginDate.getText().toString());
            try {
                holder.setBegin(beginDate.getText().toString());
            } catch (IllegalArgumentException e) {
                Log.i(ChecksRoller.LOG_TAG, "Invalid format ");
                Toast.makeText(ListActivity.this, "invalid data format", Toast.LENGTH_SHORT);
            }
            hideKeyboard(v);
            return true;
        });

        endDate = findViewById(R.id.end_date);
        endDate.setOnEditorActionListener((v, actionId, event) -> {
            Log.i(ChecksRoller.LOG_TAG, "Got end value " + endDate.getText().toString());
            try {
                holder.setEnd(endDate.getText().toString());
            } catch (IllegalArgumentException e) {
                Log.i(ChecksRoller.LOG_TAG, "Invalid format ");
                Toast.makeText(ListActivity.this, "invalid data format", Toast.LENGTH_SHORT);
            }
            hideKeyboard(v);
            return true;
        });
    }

}
