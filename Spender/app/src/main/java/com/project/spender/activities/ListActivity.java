package com.project.spender.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.project.spender.ChecksRoller;
import com.project.spender.R;
import com.project.spender.data.AppDatabase;
import com.project.spender.data.entities.CheckWithProducts;
import com.project.spender.data.entities.Product;
import com.project.spender.data.entities.ProductTagJoin;
import com.project.spender.data.entities.Tag;

import java.util.ArrayList;
import java.util.List;


public class ListActivity extends AppCompatActivity {

    private ListView listView;
    private EditText request;
    private List<CheckWithProducts> checkList;
    private AppDatabase dbManager;
    private ImageButton scan;
    private ImageButton list;
    private ImageButton statistics;

    private static final int CHOOSE_TAG_CODE = 124;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHOOSE_TAG_CODE) {
                int type = data.getIntExtra("op type", -1);
                int pos = data.getIntExtra("position", -1);
                long tagId = data.getLongExtra("tag id", -1);
                if (type == -1 || pos == -1 || tagId == -1) {
                    return;
                }
                for (Product product : checkList.get(pos).getProducts()) {
                    if (type == R.id.add_tag_for_check) {
                        ChecksRoller.getInstance().getAppDatabase()
                                .getCheckDao().insertExistingTagForProduct(tagId, product.getId());
                    } else if (type == R.id.remove_tag_for_check){
                        ChecksRoller.getInstance().getAppDatabase()
                                .getCheckDao().deleteTagProductRelation(new ProductTagJoin(product.getId(), tagId));
                    }
                }
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
        CheckWithProducts check = checkList.get(info.position);
        switch(item.getItemId()) {
            case R.id.add_tag_for_check:
            case R.id.remove_tag_for_check:
                Intent intent = new Intent(this, TagChoiceActivity.class);
                intent.putExtra("op type", item.getItemId());
                intent.putExtra("position", info.position);
                startActivityForResult(intent, CHOOSE_TAG_CODE);
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_list);

        checkList = new ArrayList<>();

        dbManager = ChecksRoller.getInstance().getAppDatabase();

        scan = findViewById(R.id.scan);
        statistics = findViewById(R.id.statistics);
        list = findViewById(R.id.list);
        list.setBackgroundColor(Color.argb(40, 255, 0, 0));

        statistics.setOnClickListener(v -> {
            Intent intent = new Intent(ListActivity.this, MainActivity.class);
            startActivity(intent);
        });

        scan.setOnClickListener(v -> {
            final Intent intent = new Intent(ListActivity.this, ScanActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivityForResult(intent, 42);
        });

        try {
            checkList = dbManager.getCheckDao().getAll();
        } catch (Exception e) {

            e.printStackTrace();
            checkList = new ArrayList<>();
        }

        listView = findViewById(R.id.productsList);
        listView.setAdapter(new ListAdapter(this, checkList));
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(ListActivity.this, CheckShowActivity.class);
            intent.putParcelableArrayListExtra("products",
                    (ArrayList<Product>) checkList.get(position).getProducts());
            startActivity(intent);
        });
        registerForContextMenu(listView);

        request = findViewById(R.id.request);
        request.setOnEditorActionListener((v, actionId, event) -> {
            String s = request.getText().toString();

            checkList.clear();
            checkList.addAll(ChecksRoller.getInstance().findCheckByRegEx(s));

            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }

            listView.invalidateViews();
            return true;
        });
    }
}
