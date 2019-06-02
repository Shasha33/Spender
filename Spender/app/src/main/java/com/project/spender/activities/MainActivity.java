package com.project.spender.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import com.project.spender.controllers.ChecksRoller;
import com.project.spender.charts.ChartsStateHolder;
import com.project.spender.fragments.LineChartFragment;
import com.project.spender.fragments.PieChartFragment;
import com.project.spender.R;
import com.project.spender.controllers.ScanResult;

import static com.project.spender.controllers.TagChoiceHelper.TAG_ID_LIST;

public class MainActivity extends AppCompatActivity implements LifecycleOwner {

    private ImageButton scan;
    private ImageButton list;
    private ImageButton statistics;
    private EditText begin;
    private EditText end;

    private LifecycleRegistry lifecycleRegistry;

    private ChartsStateHolder chartsStateHolder;

    private final static int CAMERA_REQUEST = 1;
    private final static int CHART_TAGS_CODE = 15325;
    private final static int CHECK_REQUEST = 42;

    private FragmentManager fragmentManager;
    private PieChartFragment pieFragment;
    private LineChartFragment lineFragment;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHECK_REQUEST) {

            Toast.makeText(this, ScanResult.explain(resultCode), Toast.LENGTH_SHORT).show();

        } else if (requestCode == CHART_TAGS_CODE) {

            if (data == null) {
                return;
            }
            long[] ids = data.getLongArrayExtra(TAG_ID_LIST);
            if (ids == null) {
                return;
            }
            chartsStateHolder.setIds(ids);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (requestCode == CAMERA_REQUEST) {
            if (grantResults.length == 0
                    || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission not got",
                        Toast.LENGTH_LONG).show();
                Log.i(ChecksRoller.LOG_TAG, "didnt get camera permission");
            } else {
                Log.i(ChecksRoller.LOG_TAG, "got camera permission");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (item.getItemId()) {
            case R.id.action_cheese:
                ChecksRoller.getInstance().cheese();
                return true;

            case R.id.action_delete:
                ChecksRoller.getInstance().onRemoveAllClicked();
                return true;

            case R.id.action_tag_list:
                Intent intent = new Intent(this, TagListActivity.class);
                startActivity(intent);
                return true;

            case R.id.putin:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.putout:
                ChecksRoller.getInstance().clearAccountInfo();
                break;
            case R.id.pie_chart_item:
                Log.i(ChecksRoller.LOG_TAG, "pie chart");
                fragmentTransaction.replace(R.id.fragmentHolder, pieFragment);
                fragmentTransaction.commit();
                chartsStateHolder.setChartFragment(pieFragment);
                pieFragment.drawHole(false);
                break;
            case R.id.donut_chart_item:
                Log.i(ChecksRoller.LOG_TAG, "donut chart");
                fragmentTransaction.replace(R.id.fragmentHolder, pieFragment);
                fragmentTransaction.commit();
                chartsStateHolder.setChartFragment(pieFragment);
                pieFragment.drawHole(true);
                break;
            case R.id.line_graph_item:
                Log.i(ChecksRoller.LOG_TAG, "line graph");
                fragmentTransaction.replace(R.id.fragmentHolder, lineFragment);
                fragmentTransaction.commit();
                chartsStateHolder.setChartFragment(lineFragment);
                lineFragment.invalidate();
                break;
            case R.id.tags_for_chart:
                startActivityForResult(new Intent(this, TagChoiceActivity.class), CHART_TAGS_CODE);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        lifecycleRegistry = new LifecycleRegistry(this);
        lifecycleRegistry.markState(Lifecycle.State.CREATED);


        ChecksRoller.getInstance().init(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST);
        }

        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        scan = findViewById(R.id.scan);
        list = findViewById(R.id.list);
        statistics = findViewById(R.id.statistics);

        statistics.setImageResource(R.drawable.piechart_chosen);

        list.setOnClickListener(v -> {
            final Intent intentShowList = new Intent(MainActivity.this, ListActivity.class);
            startActivity(intentShowList);
        });

        scan.setOnClickListener(v -> {
            final Intent intent = new Intent(MainActivity.this, ScanActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivityForResult(intent, CHECK_REQUEST);
        });

        begin = findViewById(R.id.begin_date_for_chart);
        end = findViewById(R.id.end_date_for_chart);

        fragmentManager = getSupportFragmentManager();
        pieFragment = PieChartFragment.newInstance();
        lineFragment = LineChartFragment.newInstance();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentHolder, pieFragment);
        fragmentTransaction.commit();

        chartsStateHolder = new ChartsStateHolder();
        chartsStateHolder.setBeginDateInput(begin);
        chartsStateHolder.setEndDateInput(end);
        chartsStateHolder.setChartFragment(pieFragment);

    }

}
