package com.example.administrator.signin_Teacher.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.signin_Teacher.R;
import com.example.administrator.signin_Teacher.module.Record;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017-01-23 .
 */

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    private List<Record> mList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView intime;
        TextView outtime;
        public ViewHolder(View view){
            super(view);
            name = (TextView)view.findViewById(R.id.name);
            intime = (TextView)view.findViewById(R.id.intime);
            outtime = (TextView)view.findViewById(R.id.outtime);
        }
    }
    public RecordAdapter(List<Record> list){mList = list;}
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recorditem,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Record record = mList.get(position);
        holder.name.setText(record.getName());
        SimpleDateFormat formatter    =   new SimpleDateFormat("yyyy年MM月dd日    HH:mm:ss");
        Date indata = new Date(record.getIn_time());//获取当前时间
        String str = formatter.format(indata);
        holder.intime.setText("签到:" + str);

        if (record.getOut_time() == 0)
            str = "未签退";
        else {
            Date outdata = new Date(record.getOut_time());
            str = "签退:" + formatter.format(outdata);
        }
        holder.outtime.setText(str);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }
}
