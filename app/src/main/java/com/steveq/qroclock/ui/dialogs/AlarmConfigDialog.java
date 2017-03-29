package com.steveq.qroclock.ui.dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.steveq.qroclock.R;
import com.steveq.qroclock.ui.activities.DataCollector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlarmConfigDialog extends DialogFragment {
    private static String TAG = AlarmConfigDialog.class.getSimpleName();
    DataCollector mDataCollector;

    public static final Integer GET_RINGTONE = 10;

    @BindView(R.id.timeInputField)
    LinearLayout timeInputTextView;

    @BindView(R.id.repeatingCheckbox)
    CheckBox repeatingCheckbox;

    @BindView(R.id.selectRingtoneField)
    LinearLayout selectRingtoneButton;

    public AlarmConfigDialog() {
        //empty constructor required for DialogFragment
    }

    public static AlarmConfigDialog newInstance(/*some arguments*/){
        AlarmConfigDialog frag = new AlarmConfigDialog();
        //pack arguments into bundle
        //Bundle args = new Bundle();
        //attach bundle into fragment
        //return fragment
        //fetch arguments later in onViewCreated();
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataCollector = (DataCollector)getActivity();
        mDataCollector.shell();
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.alarm_config_dialog, container, false);
////        timeInputTextView = (TextView) view.findViewById(R.id.timeInputTextView);
////
////        timeInputTextView.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Log.d(TAG, "Time Input Clicked");
////            }
////        });
//        return view;
//    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.alarm_config_dialog, null);
        ButterKnife.bind(this, v);

        //default logic of alert dialog is to dismiss dialog on positive OR negative button clicked
        //to implement nested dialog it was necessary to override this default behavior
        //to accomplish this, leave positive and negative buttons action as NULL ad implement
        //onShowListener outside the AlertBuilder

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                                            .setView(v)
                                            .setPositiveButton(R.string.agree, null)
                                            .setNegativeButton(R.string.disagree, null);

        AlertDialog alertDialog = builder.create();

//        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialog) {
//                Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                positiveButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        alertDialog.dismiss();
//                    }
//                });
//
//                Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                negativeButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        alertDialog.dismiss();
//                    }
//                });
//            }
//        });

;
        return alertDialog;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @OnClick(R.id.timeInputField)
    public void inputTimeClick(View v){
        MyTimePickerDialog.newInstance().show(getFragmentManager(), null);
    }

    @OnClick(R.id.repeatingCheckbox)
    public void checkboxClicked(View v) {
        if(repeatingCheckbox.isChecked()) {
            DaysRepeatingDialog.newInstance().show(getFragmentManager(), null);
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

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
