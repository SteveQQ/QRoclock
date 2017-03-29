package com.steveq.qroclock.ui.dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.steveq.qroclock.R;

public class DaysRepeatingDialog extends DialogFragment {
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
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(inflater.inflate(R.layout.repeating_days_dialog, null))
                .setPositiveButton(R.string.agree, null)
                .setNegativeButton(R.string.disagree, null);
        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
