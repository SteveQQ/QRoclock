package com.steveq.qroclock.ui.dialogs;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.steveq.qroclock.R;
import com.steveq.qroclock.repo.Alarm;
import com.steveq.qroclock.repo.Days;
import com.steveq.qroclock.repo.RepoManager;
import com.steveq.qroclock.ui.activities.DataCollector;
import com.steveq.qroclock.ui.activities.MainActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlarmConfigDialog extends DialogFragment {
    private static String TAG = AlarmConfigDialog.class.getSimpleName();
    private static String DELETABLE = "DELETABLE";
    private DataCollector mDataCollector;
    private RepoManager mRepoManager;
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
        mRepoManager = new RepoManager(mParent);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(mParent);
        View v = inflater.inflate(R.layout.alarm_config_dialog, null);
        ButterKnife.bind(this, v);

        AlertDialog.Builder builder = new AlertDialog.Builder(mParent)
                .setView(v)
                .setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Alarm a = mDataCollector.getInstance();
                        mRepoManager.saveAlarm(a);
                        mParent.formConfirmed();
                    }
                })
                .setNegativeButton(R.string.disagree, null);


        Boolean isDeletable = getArguments().getBoolean(DELETABLE);
        if(isDeletable){
            deleteAlarmImageButton.setVisibility(View.VISIBLE);
        } else {
            deleteAlarmImageButton.setVisibility(View.GONE);
        }

        return builder.create();
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
        }
    }

    @OnClick(R.id.selectRingtoneField)
    public void selectRingtone(View v){
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select ringtone for the alarm");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        startActivityForResult(intent, GET_RINGTONE);
    }

    //-------HANDLERS METHODS END---------//


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == AlarmConfigDialog.GET_RINGTONE) {
                TextView chosenRingtone = (TextView) selectRingtoneField.findViewById(R.id.chosenRingtoneTextView);

                Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                Ringtone ringtone = RingtoneManager.getRingtone(mParent, uri);

                mDataCollector.withRingtone(uri.toString());
                chosenRingtone.setText(ringtone.getTitle(mParent));
                chosenRingtone.setVisibility(View.VISIBLE);
            }
        }
    }

    public void updateTime(){
        TextView timeText = (TextView) timeInputField.findViewById(R.id.timeInputTextView);
        timeText.setText(mDataCollector.getInstance().getTime());
    }

    public void updateDaysRep(){
        TextView reps = (TextView) bodyField.findViewById(R.id.daysRepeatingTextView);
        List<Days> days = mDataCollector.getInstance().getDays();
        if(days.size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (Days d : days) {
                builder.append(d.getAbb());
                builder.append(",");
                Log.d(TAG, d.getAbb());
            }
            builder.deleteCharAt(builder.length() - 1);
            reps.setText(builder.toString());
            reps.setVisibility(View.VISIBLE);
        }
    }

}
