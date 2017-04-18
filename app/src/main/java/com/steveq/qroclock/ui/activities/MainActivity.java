package com.steveq.qroclock.ui.activities;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.j256.ormlite.dao.ForeignCollection;
import com.steveq.qroclock.R;
import com.steveq.qroclock.adapters.AlarmsRecyclerViewAdapter;
import com.steveq.qroclock.database.AlarmsManager;
import com.steveq.qroclock.repo.Alarm;
import com.steveq.qroclock.repo.Day;
import com.steveq.qroclock.service.AlarmHandlingService;
import com.steveq.qroclock.ui.dialogs.AlarmConfigDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements DataCollector{
    private static String TAG = MainActivity.class.getSimpleName();
    private Alarm mAlarm;
    private AlarmsRecyclerViewAdapter mAdapter;
    private static int GET_REFFERAL_QR = 20;

    //-------DECLARE VIEW HANDLES START--------//

    @BindView(R.id.alarmsRecyclerView)
    RecyclerView alarmsRecyclerView;

    @BindView(R.id.emptyRecyclerView)
    RelativeLayout emptyLinearLayout;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //-------DECLARE VIEW HANDLES END--------//

    //-------LIFECYCLE METHODS START--------//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        mAdapter = new AlarmsRecyclerViewAdapter(this);

        alarmsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        alarmsRecyclerView.setAdapter(mAdapter);

        prepareRecyclerViewSpace();
        AlarmsManager.getInstance(this);
    }

    //-------LIFECYCLE METHODS END--------//

    //-------CONFIG METHODS START--------//

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_calibrate) {
            Intent intent = new Intent(this, QRScannerActivity.class);
            intent.putExtra(QRScannerActivity.GET_STATE, QRScannerActivity.CALIBRATE);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //-------CONFIG METHODS END--------//

    //-------HANDLERS METHODS START----//

    @OnClick(R.id.fab)
    public void fabClick(View v){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        AlarmConfigDialog.newInstance(false, -1).show(ft, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == AlarmConfigDialog.GET_RINGTONE) {
                Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                Log.d(TAG, uri.toString());
            } else if(requestCode == GET_REFFERAL_QR){

            }
        }
    }

    //-------HANDLERS METHODS END----//

    public void formConfirmed(){
        prepareRecyclerViewSpace();
        mAdapter.update();
    }

    private void prepareRecyclerViewSpace() {
        List<Alarm> curAlarms = AlarmsManager.getInstance(this).readAlarms();
        if(curAlarms.size() == 0){
            alarmsRecyclerView.setVisibility(View.GONE);
            emptyLinearLayout.setVisibility(View.VISIBLE);
        } else if(curAlarms.size() > 0){
            alarmsRecyclerView.setVisibility(View.VISIBLE);
            emptyLinearLayout.setVisibility(View.GONE);
        }
    }


    @Override
    public void init() {
        mAlarm = new Alarm();
    }

    @Override
    public void withTime(String time) {
        mAlarm.setTime(time);
    }

    @Override
    public void withRepetition(ForeignCollection<Day> days) {
        mAlarm.setDays(days);
    }

    @Override
    public void withRingtone(String ringtone) {
        mAlarm.setRingtoneUri(ringtone);
    }

    @Override
    public Alarm getInstance() {
        return mAlarm;
    }
}
