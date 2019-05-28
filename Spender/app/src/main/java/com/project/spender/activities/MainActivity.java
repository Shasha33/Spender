package com.project.spender.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.github.mikephil.charting.charts.LineChart;
import com.project.spender.ChecksRoller;
import com.project.spender.fragments.LineChartFragment;
import com.project.spender.fragments.PieChartFragment;
import com.project.spender.R;
import com.project.spender.ScanResult;

public class MainActivity extends AppCompatActivity {

    private ImageButton scan;
    private ImageButton list;
    private ImageButton statistics;
    private ImageButton secret;
    private int clickCounter;

    private final static int MAGICCONST = 30;
    private final static int CAMERA_REQUEST = 1;
    private final static int CHART_TAGS_CODE = 15325;
    private final static int CHECK_REQUEST = 42;

    private FragmentManager fragmentManager;
    private PieChartFragment pieFragment;
    private LineChartFragment lineFragment;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHECK_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Scanned", Toast.LENGTH_SHORT).show();
            } else if (requestCode == ScanResult.NOT_ENOUGH_DATA) {
                Toast.makeText(this, "Authorization required", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Check not received", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CHART_TAGS_CODE) {
            long[] ids = data.getLongArrayExtra("tag ids");
            //(todo) update chart
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
                break;
            case R.id.donut_chart_item:
                Log.i(ChecksRoller.LOG_TAG, "donut chart");
                break;
            case R.id.line_graph_item:
                Log.i(ChecksRoller.LOG_TAG, "line graph");
                fragmentTransaction.replace(R.id.fragmentHolder, lineFragment);
                break;
            case R.id.tags_for_chart:
                startActivityForResult(new Intent(this, TagChoiceActivity.class), CHART_TAGS_CODE);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        fragmentTransaction.commit();
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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

        secret = findViewById(R.id.secret);
        secret.setOnClickListener(view -> {
            clickCounter++;
            if (clickCounter > MAGICCONST) {
                secret.setBackgroundResource(R.drawable.clevercat);
                clickCounter = 0;
            }
        });

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

        fragmentManager = getSupportFragmentManager();
        pieFragment = PieChartFragment.newInstance();
        lineFragment = LineChartFragment.newInstance();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentHolder, pieFragment);
        fragmentTransaction.commit();
    }

}
