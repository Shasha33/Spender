package com.project.spender;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Scan extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView scannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        scannerView = new ZXingScannerView(this);
        System.out.println(scannerView);
        setContentView(scannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        ScanResult scanResult = new ScanResult(result.getText());
        ChecksRoller.getInstance(getParent().getApplicationContext()).putCheck(scanResult);
        setResult(Activity.RESULT_OK);
        finish();
    }
}