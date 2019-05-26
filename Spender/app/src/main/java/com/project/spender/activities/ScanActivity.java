package com.project.spender.activities;

import android.app.Activity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;
import com.project.spender.ChecksRoller;
import com.project.spender.ScanResult;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

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
        int putResult = ChecksRoller.getInstance().putCheck(scanResult);
        if (putResult == -1) {
            setResult(ScanResult.NOT_ENOUGH_DATA);
        } else if (putResult != 0) {
            setResult(Activity.RESULT_CANCELED);
        } else {
            setResult(Activity.RESULT_OK);
        }
        finish();
    }
}