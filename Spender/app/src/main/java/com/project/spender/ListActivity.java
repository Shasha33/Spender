package com.project.spender;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.project.spender.data.ItemsDbHelper;
import com.project.spender.fns.api.Item;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ListView listView;
    private EditText request;
    private List<String> itemsList;
    private ItemsDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_list);
        itemsList = new ArrayList<>();
        dbHelper = MainActivity.dbHelper;

        try {
            for (Good i : dbHelper.getAll()) {
                itemsList.add(i.name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            itemsList = new ArrayList<>();
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_checked, itemsList);
        listView = findViewById(R.id.itemsList);
        listView.setAdapter(adapter);

        request = findViewById(R.id.request);
        request.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                try {
                    updateList(v.getText().toString());
                } catch (SQLException e) {
                    e.printStackTrace();
                    itemsList = new ArrayList<>();
                }
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                System.out.println(itemsList.size() + " " + v.getText());
                adapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void updateList(String ex) throws SQLException {
        itemsList.clear();
        for (Good i : dbHelper.getAllByName(ex)) {
            itemsList.add(i.name);
        }
    }
}
