package com.project.spender;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.project.spender.data.AppDatabase;
import com.project.spender.fns.api.NetworkManager;

public class MainActivity extends AppCompatActivity {

    private ImageButton scan;
    private ImageButton list;
    private ImageButton statistics;
    private ImageButton secret;
    private Button clear;
    private static TextView textResult;
    private AppDatabase dbManager;
    private NetworkManager networkManager;
    private int clickCounter;
    private final static int MAGICCONST = 10;
    private Button cheese;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        (todo) create constants for request code and fields names
        if (requestCode == 42) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Loaded", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Cant scan check", Toast.LENGTH_LONG).show();
            }
        }
    }
// (todo) move this somewhere


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        setContentView(R.layout.activity_main);

        networkManager = ChecksRoller.getInstance(this).getNetworkManager();

        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        dbManager = ChecksRoller.getInstance(this).getAppDatabase();

        cheese = findViewById(R.id.cheese);
        scan = findViewById(R.id.scan);
        list = findViewById(R.id.list);
        statistics = findViewById(R.id.statistics);
        secret = findViewById(R.id.secret);

        statistics.setBackgroundColor(Color.argb(40, 255, 0, 0));

        cheese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChecksRoller.getInstance(MainActivity.this).cheese();
            }
        });

        secret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Misha molodez",
                        Toast.LENGTH_LONG).show();
            }
        });

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intentShowList = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intentShowList);
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(MainActivity.this, Scan.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivityForResult(intent, 42);
            }
        });
    }
}
