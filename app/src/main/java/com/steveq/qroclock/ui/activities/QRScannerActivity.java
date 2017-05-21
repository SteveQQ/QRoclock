package com.steveq.qroclock.ui.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.steveq.qroclock.QRoclockApplication;
import com.steveq.qroclock.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QRScannerActivity extends AppCompatActivity implements BarcodeCallback {
    private static final String TAG = QRScannerActivity.class.getSimpleName();
    private static final int CAMERA_PERMISSIONS_REQUEST = 101;
    public static final String GET_STATE = "GET STATE";
    public static final int CALIBRATE = 10;
    public static final int STOP_WAKING = 20;
    private int currentState;
    public static final String REFFERAL_QR = "REFFERAL_QR";

    //@BindView(R.id.stopAlarmButton)
    //Button stopAlarmButton;

    @BindView(R.id.barcodeView)
    DecoratedBarcodeView barcodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);
        ButterKnife.bind(this);

        Intent receivedIntent = getIntent();
        currentState = receivedIntent.getIntExtra(GET_STATE, 0);

        if(currentState == STOP_WAKING){
            barcodeView.setStatusText("Scan QR to stop the alarm");
        } else if(currentState == CALIBRATE){
            barcodeView.setStatusText("Set QR for stopping alarms");
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                showExplanation("Permission Needed", "Camera", Manifest.permission.CAMERA);
            } else {
                requestPermission(Manifest.permission.CAMERA);
            }
        } else {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_LONG).show();
            barcodeView.decodeContinuous(this);
        }
    }

    private void requestPermission(String permissionName) {
        ActivityCompat.requestPermissions(this, new String[]{permissionName}, CAMERA_PERMISSIONS_REQUEST);
    }

    private void showExplanation(String title,
                                 String message,
                                 final String permission) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(Manifest.permission.READ_CONTACTS);
                    }
                });
        builder.create().show();
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
                    barcodeView.decodeSingle(this);
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Permission not granted", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    @Override
    public void barcodeResult(BarcodeResult result) {
        if(result != null){
            if(result.getText() != null){
                Log.d(TAG, "Current scanner state : " + currentState);
                String scannedQR = result.getText();
                if(currentState == CALIBRATE){
                    Log.d(TAG, "QR Calibrated with value : " + scannedQR);
                    QRoclockApplication.editor.putString(REFFERAL_QR, result.getText());
                    QRoclockApplication.editor.commit();
                    Toast.makeText(this, "QR Code Calibrated correctly", Toast.LENGTH_LONG).show();
                    finish();
                    //finishAndRemoveTask();
                } else if(currentState == STOP_WAKING){
                    String qr = QRoclockApplication.sharedPreferences.getString(REFFERAL_QR, null);
                    if(qr != null) {
                        if(qr.equals(scannedQR)){
                            Log.d(TAG, "Stop waking with qr : " + result.getText());
                            Intent intent = new Intent();
                            intent.setAction("com.steveq.qroclock.ALARM_STOP");
                            sendBroadcast(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "Scan correct QR or calibrate new!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        throw new IllegalArgumentException("QR code must be first calibrated");
                    }
                }
            }
        }
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
