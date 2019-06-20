package com.project.spender.activities;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;
import com.project.spender.data.entities.Check;
import com.project.spender.roller.App;
import com.project.spender.roller.ChecksRoller;
import com.project.spender.data.ScanResult;

import javax.inject.Inject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Activity for scanning qr-code
 */
public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    @Inject protected ChecksRoller checksRoller;
    private ZXingScannerView scannerView;

    @Override
    public void onCreate(Bundle state) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(state);
        App.getComponent().inject(this);
        scannerView = new ZXingScannerView(this);
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
        int putResult;
        try {
            ScanResult scanResult = new ScanResult(result.getText());
            putResult = checksRoller.requestCheck(scanResult);
        } catch (Exception e) {
            putResult = -2;
        }

        if (putResult == -1) {
            setResult(ScanResult.NOT_ENOUGH_DATA);
        } else if (putResult == -2) {
            setResult(ScanResult.WRONG_CODE);
        } else if (putResult != 0) {
            setResult(Activity.RESULT_CANCELED);
        } else {
            setResult(Activity.RESULT_OK);
        }
        finish();
    }
}