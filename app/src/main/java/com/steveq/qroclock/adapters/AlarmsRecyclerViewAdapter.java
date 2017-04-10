package com.steveq.qroclock.adapters;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.j256.ormlite.dao.ForeignCollection;
import com.steveq.qroclock.R;
import com.steveq.qroclock.database.AlarmsManager;
import com.steveq.qroclock.repo.Alarm;
import com.steveq.qroclock.repo.Day;
import com.steveq.qroclock.repo.Days;
import com.steveq.qroclock.ui.dialogs.AlarmConfigDialog;

import java.util.ArrayList;
import java.util.List;

public class AlarmsRecyclerViewAdapter extends RecyclerView.Adapter<AlarmsRecyclerViewAdapter.AlarmsViewHolder> {
    private static final String TAG = AlarmConfigDialog.class.getSimpleName();

    private List<Alarm> mAlarms;
    private Activity mActivity;

    public AlarmsRecyclerViewAdapter(Activity activity) {
        mActivity = activity;
        update();
    }

    public void update(){
        mAlarms = AlarmsManager.getInstance(mActivity).readAlarms();
        this.notifyDataSetChanged();
    }

    @Override
    public AlarmsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_row, parent, false);
        AlarmsViewHolder viewHolder = new AlarmsViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AlarmsViewHolder holder, int position) {
        final int pos = position;
        holder.alarmTimeTextView.setText(mAlarms.get(position).getTime());
        holder.alarmActiveSwitch.setChecked(mAlarms.get(position).getActive());
        final Alarm alarm = mAlarms.get(position);

        if(mAlarms.get(position).getDays() != null && alarm.getDays().size()>0){
            holder.daysRepsTextView.setText(AlarmConfigDialog.getDaysAbbrsString(new ArrayList<>(alarm.getDays())));
        } else {
            holder.daysRepsTextView.setText(R.string.tomorrow);
        }

        holder.alarmActiveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                alarm.setActive(true);
            } else {
                alarm.setActive(false);
            }
            AlarmsManager.getInstance(mActivity).updateAlarmActiveStatus(mAlarms.get(pos));
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = mActivity.getFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                AlarmConfigDialog.newInstance(true, mAlarms.get(pos).getId()).show(ft, null);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mAlarms.size();
    }

    class AlarmsViewHolder extends RecyclerView.ViewHolder{
        TextView alarmTimeTextView;
        Switch alarmActiveSwitch;
        TextView daysRepsTextView;

        AlarmsViewHolder(View itemView) {
            super(itemView);
            alarmTimeTextView = (TextView) itemView.findViewById(R.id.alarmTimeTextView);
            alarmActiveSwitch = (Switch) itemView.findViewById(R.id.alarmActiveSwitch);
            daysRepsTextView = (TextView) itemView.findViewById(R.id.daysRepsTextView);
        }
    }


}
