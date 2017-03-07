package com.example.administrator.signin_Teacher.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.signin_Teacher.R;
import com.example.administrator.signin_Teacher.module.StudentCourse;

import java.util.List;

/**
 * Created by Administrator on 2017-02-27 .
 */

public class UnSignOnAdapter extends RecyclerView.Adapter<UnSignOnAdapter.ViewHolder> {
    private List<StudentCourse> mList;

    public UnSignOnAdapter(List<StudentCourse> list){mList = list;}
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.un_sign_on_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StudentCourse course = mList.get(position);
        holder.name.setText(course.getName());
        holder.id.setText("学号：" + course.getsId());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView id;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.student_name);
            id = (TextView) itemView.findViewById(R.id.student_Id);
        }
    }
}
