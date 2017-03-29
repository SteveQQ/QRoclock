package com.steveq.qroclock.ui.dialogs;


import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import com.steveq.qroclock.ui.activities.DataCollector;

import java.util.Calendar;

public class MyTimePickerDialog extends DialogFragment implements android.app.TimePickerDialog.OnTimeSetListener {
    DataCollector mDataCollector;

    public MyTimePickerDialog() {
        //intentionally empty
    }

    public static MyTimePickerDialog newInstance(){
        return new MyTimePickerDialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataCollector = (DataCollector) getActivity();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog(getActivity(), this, hour, minute, true);
        tpd.setButton(TimePickerDialog.BUTTON_POSITIVE, "Apply", tpd);
        tpd.setButton(TimePickerDialog.BUTTON_NEGATIVE, "CANCEL", tpd);
        return tpd;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        StringBuilder builder = new StringBuilder();
        builder.append(hourOfDay);
        builder.append(":");
        builder.append(minute);
        mDataCollector.withTime(builder.toString());
    }
}
