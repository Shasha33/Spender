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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
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
                String[] scanningResult;
                try {
                    scanningResult = parseNumbers(data.getStringExtra("result"));
                } catch (IllegalStateException e) {
                    System.out.println(e.getMessage());
                    Toast.makeText(this, "UNABLE TO PARSE CODE INFO", Toast.LENGTH_LONG).show();
                    return;
                }
                textResult.setText(scanningResult[0] + "\n" + scanningResult[1] + "\n" + scanningResult[2]);
            } else {
                textResult.setText("(∩｀-´)⊃━☆ﾟ.*･｡ﾟ");
            }
        }
    }

    /**
     * As you can see, it returns ФН, ФД and ФП in this particular order.
     * @param content string from qr code
     */
    static protected String[] parseNumbers(String content) {
        Matcher matcherFN = Pattern.compile(".*fn=([\\d]*)&.*").matcher(content);
        Matcher matcherFD = Pattern.compile(".*i=([\\d]*)&.*").matcher(new String(content));
        Matcher matcherFP = Pattern.compile(".*fp=([\\d]*)&.*").matcher(content);

        System.out.println(matcherFN.find() + " " + matcherFD.matches() + " " + matcherFP.matches());
        String[] res = new String[3];
        res[0] = matcherFN.group(1);
        res[1] = matcherFD.group(1);
        res[2] = matcherFP.group(1);
        return res;
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
