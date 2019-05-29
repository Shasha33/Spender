package com.project.spender.activities;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.project.spender.ChecksRoller;
import com.project.spender.R;
import com.project.spender.ScanResult;
import com.project.spender.data.entities.Tag;

import java.util.List;

public class TagListActivity extends AppCompatActivity {

    private ListView listView;
    private List<Tag> tags;

    private final int NEW_TAG_CODE = 76;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == NEW_TAG_CODE) {
                updateList();
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.tag_list_menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.tag_list_context_menu, menu);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tag_list_menu, menu);
        return true;
    }

    private void updateList() {
        tags.clear();
        tags.addAll(ChecksRoller.getInstance().getAppDatabase().getCheckDao().getAllTags());
        listView.invalidateViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_tag_add:
                Intent intent = new Intent(this, NewTagActivity.class);
                startActivityForResult(intent, NEW_TAG_CODE);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Tag tag = tags.get(info.position);
        switch(item.getItemId()) {
            case R.id.tag_list_edit:
                // (todo)
                return true;
            case R.id.tag_list_delete:
                ChecksRoller.getInstance().getAppDatabase().getCheckDao().deleteTagById(tag.getId());
                tags.remove(tag);
                updateList();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_list);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tags = ChecksRoller.getInstance().getAppDatabase().getCheckDao().getAllTags();

        listView = findViewById(R.id.tag_list_menu);
        registerForContextMenu(listView);

        listView.setAdapter(new TagChoiceAdapter(this, tags));
    }
}
