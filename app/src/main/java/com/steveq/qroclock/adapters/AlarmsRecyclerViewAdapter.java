package com.steveq.qroclock.adapters;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.steveq.qroclock.R;
import com.steveq.qroclock.repo.Alarm;
import com.steveq.qroclock.repo.Alarms;
import com.steveq.qroclock.repo.Days;
import com.steveq.qroclock.repo.RepoManager;
import com.steveq.qroclock.ui.dialogs.AlarmConfigDialog;

import java.util.List;

public class AlarmsRecyclerViewAdapter extends RecyclerView.Adapter<AlarmsRecyclerViewAdapter.AlarmsViewHolder> {
    private static final String TAG = AlarmConfigDialog.class.getSimpleName();
    private RepoManager mRepoManager;
    private List<Alarm> mAlarms;
    private Activity mActivity;

    public AlarmsRecyclerViewAdapter(Activity activity, RepoManager repoManager) {
        mActivity = activity;
        mRepoManager = repoManager;
        mAlarms = mRepoManager.readAlarms().getAlarms();
    }

    public void update(){
        mAlarms = mRepoManager.readAlarms().getAlarms();
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
        holder.alarmTimeTextView.setText(mAlarms.get(position).getTime());
        holder.alarmActiveSwitch.setChecked(mAlarms.get(position).getActive());
        if(mAlarms.get(position).getDays().size()>0){
            StringBuilder builder = new StringBuilder();
            for (Days d : mAlarms.get(position).getDays()) {
                builder.append(d.getAbb());
                builder.append(",");
            }
            builder.deleteCharAt(builder.length() - 1);
            holder.daysRepsTextView.setText(builder.toString());
        } else {
            holder.daysRepsTextView.setText("Tomorrow");
        }

    }

    @Override
    public int getItemCount() {
        return mAlarms.size();
    }

    class AlarmsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView alarmTimeTextView;
        Switch alarmActiveSwitch;
        TextView daysRepsTextView;

        AlarmsViewHolder(View itemView) {
            super(itemView);
            alarmTimeTextView = (TextView) itemView.findViewById(R.id.alarmTimeTextView);
            alarmActiveSwitch = (Switch) itemView.findViewById(R.id.alarmActiveSwitch);
            daysRepsTextView = (TextView) itemView.findViewById(R.id.daysRepsTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            FragmentTransaction ft = mActivity.getFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            AlarmConfigDialog.newInstance(true).show(ft, null);
        }
    }
}
