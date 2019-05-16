package com.project.spender.activities;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.project.spender.ChecksRoller;
import com.project.spender.R;

public class MainActivity extends AppCompatActivity {

    private ImageButton scan;
    private ImageButton list;
    private ImageButton statistics;
    private ImageButton secret;
    private int clickCounter;
    private final static int MAGICCONST = 10;
    private final static int CAMERA_REQUEST = 1;
    private final static int CHECK_REQUEST = 42;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHECK_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Loaded", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Cant scan check", Toast.LENGTH_LONG).show();
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
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cheese:
                ChecksRoller.getInstance(this).cheese();
                return true;

            case R.id.action_delete:
                Toast.makeText(this, "Ask Misha's permission first",
                        Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

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

        statistics.setBackgroundColor(Color.argb(40, 255, 0, 0));

        secret.setOnClickListener(v -> {
            clickCounter++;
            if (clickCounter == MAGICCONST) {
                secret.setImageResource(R.drawable.clevercat);
                clickCounter = 0;
            } else if (clickCounter == MAGICCONST - 2) {
                Toast.makeText(MainActivity.this, "ALMOST",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                secret.setImageResource(R.drawable.cat);
            }
        });

        statistics.setOnClickListener(v -> Toast.makeText(MainActivity.this, "Misha molodez",
                Toast.LENGTH_LONG).show());

        list.setOnClickListener(v -> {
            final Intent intentShowList = new Intent(MainActivity.this, ListActivity.class);
            startActivity(intentShowList);
        });

        scan.setOnClickListener(v -> {
            final Intent intent = new Intent(MainActivity.this, ScanActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivityForResult(intent, CHECK_REQUEST);
        });
    }
}
