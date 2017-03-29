package com.steveq.qroclock.ui.dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.steveq.qroclock.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class AlarmConfigDialog extends DialogFragment {
    private static String TAG = AlarmConfigDialog.class.getSimpleName();

    @Nullable @BindView(R.id.timeInputTextView)
    TextView timeInputTextView;

    @Nullable @BindView(R.id.selectRingtoneButton)
    Button selectRingtoneButton;

    @Nullable @BindView(R.id.repeatingCheckbox)
    CheckBox repeatingCheckbox;

    public AlarmConfigDialog() {
        //empty constructor required for DialogFragment
    }

    public static AlarmConfigDialog newInstance(/*some arguments*/){
        AlarmConfigDialog frag = new AlarmConfigDialog();
        //pack arguments into bundle
        Bundle args = new Bundle();
        //attach bundle into fragment
        //return fragment
        //fetch arguments later in onViewCreated();
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                                            .setView(inflater.inflate(R.layout.alarm_config_dialog, null))
                                            .setPositiveButton(R.string.agree, null)
                                            .setNegativeButton(R.string.disagree, null);

        final AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                                                                                    .setTitle("Hello")
                                                                                    .setPositiveButton(R.string.agree, null)
                                                                                    .setNegativeButton(R.string.disagree, null);
                        builder.create().show();
                    }
                });

                Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });
//                                            .setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
////                                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
////                                                    ft.addToBackStack(null);
////                                                    DaysRepeatingDialog.newInstance().show(ft, null);
//                                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
//                                                                                    .setTitle("Hello")
//                                                                                    .setPositiveButton(R.string.agree, null)
//                                                                                    .setNegativeButton(R.string.disagree, null);
//                                                    builder.create().show();
//
//                                                }
//                                            })
//                                            .setNegativeButton(R.string.disagree, null);
        return alertDialog;
    }

    @Optional
    @OnClick(R.id.timeInputTextView)
    public void inputTimeClick(View v){
        Log.d(TAG, "Time Input Clicked");
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
