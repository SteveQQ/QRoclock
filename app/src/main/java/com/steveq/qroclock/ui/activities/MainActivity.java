package com.steveq.qroclock.ui.activities;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.steveq.qroclock.R;
import com.steveq.qroclock.repo.Alarm;
import com.steveq.qroclock.repo.Alarms;
import com.steveq.qroclock.repo.RepoManager;
import com.steveq.qroclock.ui.dialogs.AlarmConfigDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements DataCollector{
    private static String TAG = MainActivity.class.getSimpleName();
    private View dialogView;
    private Alarm mAlarm;

    @Nullable @BindView(R.id.alarmsRecyclerView)
    RecyclerView alarmsRecyclerView;

    @Nullable @BindView(R.id.emptyRecyclerView)
    LinearLayout emptyLinearLayout;


//    private TextView timeInputTextView;
//    private Button selectRingtoneButton;

    RepoManager mManager;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialogView = LayoutInflater.from(this).inflate(R.layout.alarm_config_dialog, null, false);
        ButterKnife.bind(this);
        ButterKnife.bind(this, dialogView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmConfigDialog.newInstance().show(getFragmentManager(), null);
            }
        });
        mManager = new RepoManager(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == AlarmConfigDialog.GET_RINGTONE) {
                Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                Log.d(TAG, uri.toString());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void shell() {
        mAlarm = new Alarm();
    }

    @Override
    public void withTime(String time) {
        mAlarm.setTime(time);
    }

    @Override
    public void withRepetition(List<String> days) {
        mAlarm.setDays(days);
    }

    @Override
    public void withRingtone(String ringtone) {
        mAlarm.setRingtoneUri(ringtone);
    }

    @Override
    public Alarm build() {
        return mAlarm;
    }
}
