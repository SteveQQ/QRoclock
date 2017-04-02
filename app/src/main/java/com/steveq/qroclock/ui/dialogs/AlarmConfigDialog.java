package com.steveq.qroclock.ui.dialogs;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.ForeignCollection;
import com.steveq.qroclock.R;
import com.steveq.qroclock.database.AlarmsManager;
import com.steveq.qroclock.repo.Alarm;
import com.steveq.qroclock.repo.Day;
import com.steveq.qroclock.repo.Days;
import com.steveq.qroclock.ui.activities.DataCollector;
import com.steveq.qroclock.ui.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlarmConfigDialog extends DialogFragment {
    private static String TAG = AlarmConfigDialog.class.getSimpleName();
    private static String DELETABLE = "DELETABLE";
    private DataCollector mDataCollector;
    private MainActivity mParent;

    public static final Integer GET_RINGTONE = 10;

    @BindView(R.id.bodyField)
    LinearLayout bodyField;

    @BindView(R.id.timeInputField)
    LinearLayout timeInputField;

    @BindView(R.id.repeatingCheckbox)
    CheckBox repeatingCheckbox;

    @BindView(R.id.selectRingtoneField)
    LinearLayout selectRingtoneField;

    @BindView(R.id.deleteAlarmImageButton)
    ImageButton deleteAlarmImageButton;

    @BindView(R.id.daysRepeatingTextView)
    TextView daysRepeatingTextView;

    public AlarmConfigDialog() {
        //empty constructor required for DialogFragment
    }

    public static AlarmConfigDialog newInstance(Boolean isDeletable){
        AlarmConfigDialog frag = new AlarmConfigDialog();
        Bundle args = new Bundle();
        args.putBoolean(DELETABLE, isDeletable);
        frag.setArguments(args);
        return frag;
    }

    //-------LIFECYCLE METHODS START--------//

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParent = (MainActivity) getActivity();
        mDataCollector = mParent;
        mDataCollector.init();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(mParent);
        View v = inflater.inflate(R.layout.alarm_config_dialog, null);
        ButterKnife.bind(this, v);

        AlertDialog.Builder builder = new AlertDialog.Builder(mParent)
                .setView(v)
                .setPositiveButton(R.string.agree, null)
                .setNegativeButton(R.string.disagree, null);

        final AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positioveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                positioveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Alarm a = mDataCollector.getInstance();
                        if(a.getTime() != null && !a.getTime().equals("")){
                            //mRepoManager.saveAlarm(a);
                            Integer id = AlarmsManager.getInstance(getActivity()).createAlarm(a);
                            if(mDataCollector.getInstance().getTempDays().size() > 0) {
                                Alarm alarmResult = AlarmsManager.getInstance(getActivity()).readAlarmById(id);
                                ForeignCollection<Day> days = alarmResult.getDays();
                                for(Day d : mDataCollector.getInstance().getTempDays()){
                                    days.add(d);
                                }
                                AlarmsManager.getInstance(getActivity()).updateAlarm(alarmResult);
                            }
                            mParent.formConfirmed();
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(mParent, "You should provide alarm time first...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });

        Boolean isDeletable = getArguments().getBoolean(DELETABLE);
        if(isDeletable){
            deleteAlarmImageButton.setVisibility(View.VISIBLE);
        } else {
            deleteAlarmImageButton.setVisibility(View.GONE);
        }
        updateRingtone();

        return alertDialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    //-------LIFECYCLE METHODS END--------//

    //-------HANDLERS METHODS START-------//

    @OnClick(R.id.timeInputField)
    public void inputTimeClick(View v){
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        MyTimePickerDialog.newInstance().show(ft, null);
    }

    @OnClick(R.id.repeatingCheckbox)
    public void checkboxClicked(View v) {
        if(repeatingCheckbox.isChecked()) {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            DaysRepeatingDialog.newInstance().show(ft, null);
        } else {
            mDataCollector.getInstance().setTempDays(new ArrayList<Day>());
            daysRepeatingTextView.setText("");
        }
    }

    @OnClick(R.id.selectRingtoneField)
    public void selectRingtone(View v){
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select ringtone for the alarm");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        startActivityForResult(intent, GET_RINGTONE);
    }

    //-------HANDLERS METHODS END---------//


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == AlarmConfigDialog.GET_RINGTONE) {
                Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                mDataCollector.withRingtone(uri.toString());

                updateRingtone();

                Log.d(TAG, uri.toString());
            }
        }
    }

    public void updateRingtone(){
        TextView chosenRingtone = (TextView) selectRingtoneField.findViewById(R.id.chosenRingtoneTextView);
        Uri uri = Uri.parse(mDataCollector.getInstance().getRingtoneUri());

        Ringtone ringtone = RingtoneManager.getRingtone(mParent, uri);
        chosenRingtone.setText(ringtone.getTitle(mParent));
    }

    public void updateTime(){
        TextView timeText = (TextView) timeInputField.findViewById(R.id.timeInputTextView);
        timeText.setText(mDataCollector.getInstance().getTime());
    }

    public void updateDaysRep(){
        //TextView reps = (TextView) bodyField.findViewById(R.id.daysRepeatingTextView);
        List<Day> days = mDataCollector.getInstance().getTempDays();
        if(days.size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (Day d : days) {
                builder.append(Days.valueOf(d.getDayName()).getAbb());
                builder.append(",");
            }
            builder.deleteCharAt(builder.length() - 1);
            daysRepeatingTextView.setText(builder.toString());
            daysRepeatingTextView.setVisibility(View.VISIBLE);
        }
    }

}
