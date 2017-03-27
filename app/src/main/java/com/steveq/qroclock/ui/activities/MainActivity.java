package com.steveq.qroclock.ui.activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.steveq.qroclock.R;
import com.steveq.qroclock.repo.Alarms;
import com.steveq.qroclock.repo.RepoManager;

import java.util.Calendar;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class MainActivity extends AppCompatActivity {
    private static String TAG = MainActivity.class.getSimpleName();
    private static Integer GET_RINGTONE = 10;
    private View dialogView;

    @Nullable @BindView(R.id.alarmsRecyclerView)
    RecyclerView alarmsRecyclerView;

    @Nullable @BindView(R.id.emptyRecyclerView)
    LinearLayout emptyLinearLayout;

    @Nullable @BindView(R.id.timeInputTextView)
    TextView timeInputTextView;

    @Nullable @BindView(R.id.selectRingtoneButton)
    Button selectRingtoneButton;

//    private TextView timeInputTextView;
//    private Button selectRingtoneButton;

    RepoManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialogView = LayoutInflater.from(this).inflate(R.layout.alarm_config_dialog, null, false);
        ButterKnife.bind(this);
        ButterKnife.bind(this, dialogView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        timeInputTextView = (TextView) dialogView.findViewById(R.id.timeInputTextView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Alarms a = mManager.readAlarms();
                Log.d(TAG, a.toString());
                new MaterialDialog.Builder(MainActivity.this)
                        .title("Title")
                        .customView(dialogView, true)
                        .positiveText(R.string.agree)
                        .negativeText(R.string.disagree)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            }
                        })
                        .show();
//                Intent intent = new Intent(MainActivity.this, TimeTrackerService.class);
//                intent.putExtra(TimeTrackerService.EXTRA_MESSAGE, "THIS IS SERVICE");
//                startService(intent);
            }
        });
        mManager = new RepoManager(this);
    }

    @Optional @OnClick(R.id.timeInputTextView)
    public void inputTimeClick(View v){
        Log.d(TAG, "Time Input Clicked");
    }

    @Optional @OnClick(R.id.selectRingtoneButton)
    public void selectRingtone(View v){
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select ringtone for the alarm");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        startActivityForResult(intent, GET_RINGTONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == GET_RINGTONE) {
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
}
