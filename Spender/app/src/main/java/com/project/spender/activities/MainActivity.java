package com.project.spender.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.project.spender.ChecksRoller;
import com.project.spender.PieChartController;
import com.project.spender.PieChartFragment;
import com.project.spender.R;
import com.project.spender.ScanResult;
import com.project.spender.data.CheckDao;

public class MainActivity extends AppCompatActivity {

    private ImageButton scan;
    private ImageButton list;
    private ImageButton statistics;
    private ImageButton secret;
    private int clickCounter;

    private final static int MAGICCONST = 30;
    private final static int CAMERA_REQUEST = 1;
    private final static int CHECK_REQUEST = 42;

    private FragmentManager fragmentManager;
    private PieChartFragment pieFragment;

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
            case R.id.putout:
                ChecksRoller.clearAccountInfo();

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ChecksRoller.init(this);

        Log.i(ChecksRoller.LOG_TAG, "KEK");

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
        statistics.setBackgroundColor(Color.argb(40, 255, 0, 0));

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
        pieFragment = new PieChartFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentHolder, pieFragment);
        fragmentTransaction.commit();
    }

}
