package com.project.spender;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//import com.project.spender.data.ItemsDbHelper;

public class ListActivity extends AppCompatActivity {

    private ListView listView;
    private EditText request;
    private List<String> itemsList;

//    private ItemsDbHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_list);
        itemsList = new ArrayList<>();
//        dbHelper = MainActivity.dbHelper;
//
//        try {
//            for (Product i : dbHelper.getAll()) {
//                itemsList.add(i.name);
//            }
//        } catch (Exception e) {
//
//            e.printStackTrace();
//            itemsList = new ArrayList<>();
//        }
//
//        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_list_item_1, itemsList);
//        listView = findViewById(R.id.itemsList);
//        listView.setAdapter(adapter);
//
//        request = findViewById(R.id.request);
//        request.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                try {
//                    updateList(v.getText().toString());
//                } catch (SQLException e) {
//                    System.out.println(e.getMessage());
//                    itemsList = new ArrayList<>();
//                }
//                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm != null) {
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                }
//                v.setText("");
//                System.out.println(itemsList.size() + " " + v.getText());
//                adapter.notifyDataSetChanged();
//                return true;
//            }
//        });
    }

    private void updateList(String ex) throws SQLException {
//        itemsList.clear();
//        for (Product i : dbHelper.getAllByName(ex)) {
//            itemsList.add(i.name);
//        }
    }
}
