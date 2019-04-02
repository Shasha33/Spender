package com.project.spender;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.service.notification.StatusBarNotification;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private Button scan;
    private Button statstic;
    private static TextView textResult;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 42) {
            if (resultCode == Activity.RESULT_OK) {
                List<String> scanningResult;
                try {
                    scanningResult = parseNumbers(data.getStringExtra("result"));
                } catch (IllegalStateException e) {
                    System.out.println(e.getMessage());
                    Toast.makeText(this, "UNABLE TO PARSE CODE INFO", Toast.LENGTH_LONG).show();
                    return;
                }
            } else {
                textResult.setText("(∩｀-´)⊃━☆ﾟ.*･｡ﾟ");
            }
        }
    }

    static protected List<String> parseNumbers(String content) {
        List<String> res = new ArrayList<>();
        for (String i : content.split("[&|=|a-zA-z| ]")) {
            if (i.length() != 0) {
                if (i.contains(".")) {
                    res.add(i.replace(".", ""));
                } else {
                    res.add(i);
                }
            }
        }
        return res;
    }

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

        scan = findViewById(R.id.scan);
        statstic = findViewById(R.id.statistic);
        textResult = findViewById(R.id.resultText);

        final Intent intent = new Intent(this, Scan.class);
        final Intent intentShowList = new Intent(this, ListActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        statstic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentShowList);
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(intent, 42);
            }
        });
    }
}
