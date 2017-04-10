package com.steveq.qroclock.ui.dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.j256.ormlite.dao.ForeignCollection;
import com.steveq.qroclock.R;
import com.steveq.qroclock.database.AlarmsManager;
import com.steveq.qroclock.repo.Alarm;
import com.steveq.qroclock.repo.Day;
import com.steveq.qroclock.repo.Days;
import com.steveq.qroclock.ui.activities.DataCollector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DaysRepeatingDialog extends DialogFragment {
    private static final String TAG = DaysRepeatingDialog.class.getSimpleName();
    Set<Days> checklist;
    private DataCollector mDataCollector;
    private AlarmConfigDialog mParentDialog;

    @BindView(R.id.mondayCheckbox)
    CheckBox mondayCheckbox;

    @BindView(R.id.tuesdayCheckbox)
    CheckBox tuesdayCheckbox;

    @BindView(R.id.wednesdayCheckbox)
    CheckBox wednesdayCheckbox;

    @BindView(R.id.thursdayCheckbox)
    CheckBox thursdayCheckbox;

    @BindView(R.id.fridayCheckbox)
    CheckBox fridayCheckbox;

    @BindView(R.id.saturdayCheckbox)
    CheckBox saturdayCheckbox;

    @BindView(R.id.sundayCheckbox)
    CheckBox sundayCheckbox;

    public DaysRepeatingDialog() {
        //empty constructor required for DialogFragment
    }

    public static DaysRepeatingDialog newInstance(){
        DaysRepeatingDialog frag = new DaysRepeatingDialog();
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checklist = new TreeSet<>();
        mDataCollector = (DataCollector) getActivity();
        mParentDialog = (AlarmConfigDialog) getParentFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.repeating_days_dialog, null);
        ButterKnife.bind(this, v);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(R.string.agree, null)
                .setNegativeButton(R.string.disagree, null);

        return builder.create();
    }

    @OnClick(R.id.mondayCheckbox)
    public void mondayClicked(View v){
        if(mondayCheckbox.isChecked()){
            checklist.add(Days.MONDAY);
        } else {
            checklist.remove(Days.MONDAY);
        }
    }

    @OnClick(R.id.tuesdayCheckbox)
    public void tuesdayClicked(View v){
        if(tuesdayCheckbox.isChecked()){
            checklist.add(Days.TUESDAY);
        } else {
            checklist.remove(Days.TUESDAY);
        }
    }

    @OnClick(R.id.wednesdayCheckbox)
    public void wednesdayClicked(View v){
        if(wednesdayCheckbox.isChecked()){
            checklist.add(Days.WEDNESDAY);
        } else {
            checklist.remove(Days.WEDNESDAY);
        }
    }

    @OnClick(R.id.thursdayCheckbox)
    public void thursdayClicked(View v){
        if(thursdayCheckbox.isChecked()){
            checklist.add(Days.THURSDAY);
        } else {
            checklist.remove(Days.THURSDAY);
        }
    }

    @OnClick(R.id.fridayCheckbox)
    public void fridayClicked(View v){
        if(fridayCheckbox.isChecked()){
            checklist.add(Days.FRIDAY);
        } else {
            checklist.remove(Days.FRIDAY);
        }
    }

    @OnClick(R.id.saturdayCheckbox)
    public void saturdayClicked(View v){
        if(saturdayCheckbox.isChecked()){
            checklist.add(Days.SATURDAY);
        } else {
            checklist.remove(Days.SATURDAY);
        }
    }

    @OnClick(R.id.sundayCheckbox)
    public void sundayClicked(View v){
        if(sundayCheckbox.isChecked()){
            checklist.add(Days.SUNDAY);
        } else {
            checklist.remove(Days.SUNDAY);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        List<Day> ds = new ArrayList<>();
        for(Days d : checklist){
            ds.add(new Day(d.toString()));
        }

        mDataCollector.getInstance().setTempDays(ds);
        mParentDialog.updateDaysRep();
    }
}
