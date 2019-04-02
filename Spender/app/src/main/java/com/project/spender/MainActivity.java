package com.project.spender;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {

    private Button scan;
    private Button statstic;
    private static TextView textResult;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 42) {
            if (resultCode == Activity.RESULT_OK) {
                String scanningResult = data.getStringExtra("result");
                textResult.setText(parseNumbers(scanningResult)[0]);
            } else {
                textResult.setText("(∩｀-´)⊃━☆ﾟ.*･｡ﾟ");
            }
        }
    }

    static private String[] parseNumbers(String content) {
//        TODO сумма без точки
        String[] result = new String[3];
        String[] parseResult = content.split("[&=]");
        for (int i = 0; i < parseResult.length; i++) {
            if (parseResult[i].equals("fn")) {
                result[0] = parseResult[i+1];
                i++;
            } else if (parseResult[i].equals("i")) {
                result[1] = parseResult[i+1];
                i++;
            } else if (parseResult[i].equals("fp")) {
                result[1] = parseResult[i+1];
                i++;
            }
        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        final Toast toast = Toast.makeText(this, "Kekos knchn", Toast.LENGTH_LONG);

        statstic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast.show();
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
