package com.project.spender;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.StrictMode;
import android.service.notification.StatusBarNotification;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.project.spender.fns.api.Check;
import com.project.spender.fns.api.NetworkManager;

import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.SQLException;
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
    private NetworkManager networkManager;
    protected static DataBase dataBase;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 42) {
            if (resultCode == Activity.RESULT_OK) {
                List<String> scanningResult = parseNumbers(data.getStringExtra("result"));

                String fn = scanningResult.get(2);
                String fd = scanningResult.get(3);
                String fp = scanningResult.get(4);
                String date = scanningResult.get(0);
                String sum = scanningResult.get(1);

                try {
                    if (!networkManager.isCheckExist(fn, fd, fp, date, sum)) {
                        Toast.makeText(this, "Check does not exist",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    Check check = networkManager.getCheck(fn, fd, fp, date, sum);
                    dataBase.parseGoodFromCheck(check);
                } catch (SQLException e) {
                    System.out.println("KEK! " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Error while loading check " + e.getMessage() + " " + e.getCause() + " " + e.getClass());
                }
            } else {
                textResult.setText("(∩｀-´)⊃━☆ﾟ.*･｡ﾟ");
            }
        }
    }

    static protected List<String> parseNumbers(String content) {
        List<String> res = new ArrayList<>();
        for (String i : content.split("[&|=|a-z]")) {
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

        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        networkManager = NetworkManager.getInstance();
        try {
            dataBase = new DataBase();
        } catch (SQLException e) {
            System.out.println(e.getMessage() + " AAAA!");
        }
        System.out.println(dataBase);
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
