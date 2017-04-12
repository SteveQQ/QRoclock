package com.steveq.qroclock.ui.activities;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.steveq.qroclock.R;
import com.steveq.qroclock.service.AlarmHandlingService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlarmRingActivity extends AppCompatActivity {
    private static final String TAG = AlarmRingActivity.class.getSimpleName();

    @BindView(R.id.stopAlarmButton)
    Button stopAlarmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ring);
        ButterKnife.bind(this);

        Intent receivedIntent = getIntent();

    }

    @OnClick(R.id.stopAlarmButton)
    public void stopAlarm(View v){
        Intent intent = new Intent();
        intent.setAction("com.steveq.qroclock.ALARM_STOP");
        sendBroadcast(intent);
    }
}
