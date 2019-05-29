package com.project.spender.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.project.spender.CheckListHolder;
import com.project.spender.ChecksRoller;
import com.project.spender.R;
import com.project.spender.data.entities.CheckWithProducts;
import com.project.spender.data.entities.Product;

import java.util.ArrayList;

import static com.project.spender.charts.ChartsStateHolder.hideKeyboard;


public class ListActivity extends AppCompatActivity {

    private ListView listView;
    private EditText request;

    private CheckListHolder holder;

    private ImageButton scan;
    private ImageButton list;
    private ImageButton statistics;
    private EditText beginDate;
    private EditText endDate;

    private static final int CHOOSE_TAG_CODE = 124;
    private static final int CHOOSE_TAG_FOR_SHOW = 125;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHOOSE_TAG_CODE) {
                int type = data.getIntExtra("op type", -1);
                int pos = data.getIntExtra("position", -1);
                long[] tagIds = data.getLongArrayExtra("tag ids");
                if (type == -1 || pos == -1 || tagIds == null) {
                    return;
                }
                Log.i(ChecksRoller.LOG_TAG, "choose tag" + tagIds.length + " to " + pos );
                if (type == R.id.add_tag_for_check) {
                    holder.addTags(pos, tagIds);
                } else if (type == R.id.remove_tag_for_check) {
                    holder.removeTags(pos, tagIds);
                }
            } else if (requestCode == CHOOSE_TAG_FOR_SHOW) {
                long[] tagIds = data.getLongArrayExtra("tag ids");
                holder.setTags(tagIds);
            }
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.list_view_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        CheckWithProducts check = holder.getList().get(info.position);
        switch(item.getItemId()) {
            case R.id.add_tag_for_check:
            case R.id.remove_tag_for_check:
                Intent intent = new Intent(this, TagChoiceActivity.class);
                intent.putExtra("op type", item.getItemId());
                intent.putExtra("position", info.position);
                startActivityForResult(intent, CHOOSE_TAG_CODE);
                break;
            case R.id.remove_check:
                ChecksRoller.getInstance().getAppDatabase().getCheckDao().deleteCheckById(check.getCheck().getId());
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


        scan = findViewById(R.id.scan);
        statistics = findViewById(R.id.statistics);
        list = findViewById(R.id.list);
        list.setImageResource(R.drawable.history_chosen);

        statistics.setOnClickListener(v -> {
            Intent intent = new Intent(ListActivity.this, MainActivity.class);
            startActivity(intent);
        });

        scan.setOnClickListener(v -> {
            final Intent intent = new Intent(ListActivity.this, ScanActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivityForResult(intent, 42);
        });

        listView = findViewById(R.id.productsList);
        registerForContextMenu(listView);

        holder = new CheckListHolder(listView, this);

        request = findViewById(R.id.request);
        request.setOnEditorActionListener((v, actionId, event) -> {

            holder.setSubstring(request.getText().toString());

            hideKeyboard(v);

            listView.invalidateViews();
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

            listView.invalidateViews();
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

            listView.invalidateViews();
            return true;
        });
    }

}
