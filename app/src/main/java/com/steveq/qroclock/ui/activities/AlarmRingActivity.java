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

    private Ringtone mRingtone;
    private Boolean isWaking = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ring);
        ButterKnife.bind(this);

        Intent receivedIntent = getIntent();

        Uri ringUri = Uri.parse(receivedIntent.getStringExtra(AlarmHandlingService.ALARM_RINGTONE));
        mRingtone = RingtoneManager.getRingtone(this, ringUri);
    }

    @Override
    protected void onResume() {
        super.onResume();
        wakeUp();
    }

    private void wakeUp(){
        final AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        HandlerThread thread = new HandlerThread("Ringtone play thread");
        thread.start();
        Handler handler = new Handler(thread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                while(isWaking){
                    if(!mRingtone.isPlaying()) {
                        mRingtone.play();
                        audioManager.setStreamVolume(AudioManager.STREAM_RING, audioManager.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
                    }
                }
            }
        });
    }

    @OnClick(R.id.stopAlarmButton)
    public void stopAlarm(View v){
        isWaking = false;
        mRingtone.stop();
    }
}
