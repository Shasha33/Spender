package com.project.spender;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.project.spender.fns.api.NetworkManager;

public class MainActivity extends AppCompatActivity {

    private ImageButton scan;
    private ImageButton list;
    private ImageButton statistics;
    private ImageButton secret;
    private Button clear;
    private static TextView textResult;
    private NetworkManager networkManager;
//    protected static ItemsDbHelper dbHelper;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == 42) {
//            if (resultCode == Activity.RESULT_OK) {
//                List<String> scanningResult = parseNumbers(data.getStringExtra("result"));
//
//                final String fn = scanningResult.get(2);
//                final String fd = scanningResult.get(3);
//                final String fp = scanningResult.get(4);
//                final String date = scanningResult.get(0);
//                final String sum = scanningResult.get(1);
//
//
//                System.out.println(fn + " " + fd + " " + fp + " " + date + " " + sum);
//
//                try {
//                    if (!networkManager.isCheckExistCodeSync(fn, fd, fp, date, sum)) {
//                        Toast.makeText(this, "CheckJson does not exist",
//                                Toast.LENGTH_LONG).show();
//                        return;
//                    }
//                    CheckJson checkJson = networkManager.getCheckSync(fn, fd, fp, date, sum);;
//                    parseGoodFromCheck(checkJson);
//                } catch (Throwable e) {
//                    Toast.makeText(this, "Error while loading check " + e.getMessage() +
//                            " " + e.getCause() + " " + e.getClass(), Toast.LENGTH_LONG).show();
//                }
//                Toast.makeText(this, "Loaded", Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(this, "Cant scan check", Toast.LENGTH_LONG).show();
//            }
//        }
    }

//    public void parseGoodFromCheck(CheckJson checkJson) {
//        Receipt receipt = checkJson.getData();
//        System.out.println(receipt.dateTime);
//        for (Item item : receipt.items) {
//            dbHelper.insertItem(item.name, receipt.dateTime, item.price, receipt.retailPlaceAddress);
//        }
//    }
//
//    static protected List<String> parseNumbers(String content) {
//        List<String> res = new ArrayList<>();
//        System.out.println(content);
//        for (String i : content.split("[&|=|a-z]")) {
//            if (i.length() != 0) {
//                if (i.contains(".")) {
//                    res.add(i.replace(".", ""));
//                } else {
//                    res.add(i);
//                }
//            }
//        }
//        return res;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//        View decorView = getWindow().getDecorView();
//        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
//        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//
//        ActionBar actionBar = getActionBar();
//        if (actionBar != null) {
//            actionBar.hide();
//        }
//
//        setContentView(R.layout.activity_main);
//
//
//        StrictMode.ThreadPolicy policy =
//                new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//
//        networkManager = NetworkManager.getInstance();
//
//        dbHelper = new ItemsDbHelper(this);
//
//        clear = findViewById(R.id.clear);
//        scan = findViewById(R.id.scan);
//        list = findViewById(R.id.list);
//        statistics = findViewById(R.id.statistics);
//        textResult = findViewById(R.id.resultText);
//        secret = findViewById(R.id.secret);
//
//        secret.setOnClickListener(new View.OnClickListener() {
//            int cnt = 0;
//
//            @Override
//            public void onClick(View v) {
//                cnt++;
//                if (cnt % 2 == 1) {
//                    secret.setImageResource(R.drawable.clevercat);
//                } else {
//                    secret.setImageResource(R.drawable.cat);
//                }
//            }
//        });
//
//        statistics.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Misha molodez",
//                        Toast.LENGTH_LONG).show();
//            }
//        });
//
//        clear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dbHelper.clear();
//                Toast.makeText(MainActivity.this, "All items removed successfully",
//                        Toast.LENGTH_LONG).show();
//            }
//        });
//
//        list.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Intent intentShowList = new Intent(MainActivity.this, ListActivity.class);
//                startActivity(intentShowList);
//            }
//        });
//
//        scan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Intent intent = new Intent(MainActivity.this, Scan.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                startActivityForResult(intent, 42);
//            }
//        });
    }
}
