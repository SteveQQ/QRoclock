package com.steveq.qroclock.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.BarcodeView;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.steveq.qroclock.R;
import com.steveq.qroclock.service.AlarmHandlingService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlarmRingActivity extends AppCompatActivity implements BarcodeCallback {
    private static final String TAG = AlarmRingActivity.class.getSimpleName();
    private static final int CAMERA_PERMISSIONS_REQUEST = 101;

    //@BindView(R.id.stopAlarmButton)
    //Button stopAlarmButton;

    @BindView(R.id.barcodeView)
    DecoratedBarcodeView barcodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ring);
        ButterKnife.bind(this);

        Intent receivedIntent = getIntent();

        barcodeView.setStatusText("Scan Barcode");

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, CAMERA_PERMISSIONS_REQUEST);
        } else {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_LONG).show();
            barcodeView.decodeContinuous(this);
        }

        //new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onResume() {
        super.onResume();

        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeView.pause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case CAMERA_PERMISSIONS_REQUEST:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    barcodeView.decodeContinuous(this);
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Permission not granted", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void barcodeResult(BarcodeResult result) {
        Toast.makeText(this, result.getText(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void possibleResultPoints(List<ResultPoint> resultPoints) {

    }

    //    @OnClick(R.id.stopAlarmButton)
//    public void stopAlarm(View v){
//        Intent intent = new Intent();
//        intent.setAction("com.steveq.qroclock.ALARM_STOP");
//        sendBroadcast(intent);
//    }
}
