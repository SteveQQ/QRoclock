package com.steveq.qroclock.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.steveq.qroclock.R;
import com.steveq.qroclock.repo.Alarm;
import com.steveq.qroclock.repo.Alarms;
import com.steveq.qroclock.repo.RepoManager;

import java.util.List;

public class AlarmsRecyclerViewAdapter extends RecyclerView.Adapter<AlarmsRecyclerViewAdapter.AlarmsViewHolder>{

    private RepoManager mRepoManager;
    private List<Alarm> mAlarms;

    public AlarmsRecyclerViewAdapter(RepoManager repoManager) {
        mRepoManager = repoManager;
        mAlarms = mRepoManager.readAlarms().getAlarms();
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
    }

    @Override
    public int getItemCount() {
        return mAlarms.size();
    }

    class AlarmsViewHolder extends RecyclerView.ViewHolder {
        TextView alarmTimeTextView;
        Switch alarmActiveSwitch;

        AlarmsViewHolder(View itemView) {
            super(itemView);
            alarmTimeTextView = (TextView) itemView.findViewById(R.id.alarmTimeTextView);
            alarmActiveSwitch = (Switch) itemView.findViewById(R.id.alarmActiveSwitch);
        }
    }
}
