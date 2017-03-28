package com.steveq.qroclock.ui.dialogs;


import android.app.Dialog;
import android.app.DialogFragment;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alarm_config_dialog, container);
        ButterKnife.bind(getActivity(), view);

        return view;
    }

    @Optional
    @OnClick(R.id.timeInputTextView)
    public void inputTimeClick(View v){
        Log.d(TAG, "Time Input Clicked");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
