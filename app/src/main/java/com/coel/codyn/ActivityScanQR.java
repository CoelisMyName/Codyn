package com.coel.codyn;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ActivityScanQR extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    public static final String EXTRA_QR_STRING = "com.coel.codyn.EXTRA_QR_STRING";
    public static final int CAMERA_PERMISSION_REQUEST = 0;
    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
    }

    @Override
    public void handleResult(Result result) {
        Intent data = new Intent();
        data.putExtra(EXTRA_QR_STRING, result.getText());
        setResult(Activity.RESULT_OK, data);
        Log.d("handleResult", result.getText());
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            for (int i : grantResults) {
                if (i != PackageManager.PERMISSION_GRANTED) {
                    finish();
                    return;
                }
            }
            scannerView.setResultHandler(this);
            scannerView.startCamera();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        scannerView.startCamera();
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST);
        } else {
            scannerView.setResultHandler(this);
            scannerView.startCamera();
        }
    }

}
