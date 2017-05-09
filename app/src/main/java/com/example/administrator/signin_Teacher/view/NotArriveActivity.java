package com.example.administrator.signin_Teacher.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.administrator.signin_Teacher.R;
import com.example.administrator.signin_Teacher.adapter.CountNotArriveAdapter;
import com.example.administrator.signin_Teacher.adapter.NotArriveAdapter;
import com.example.administrator.signin_Teacher.module.CountNotArrive;
import com.example.administrator.signin_Teacher.module.NotArrive;
import com.example.administrator.signin_Teacher.module.StudentCourse;
import com.example.administrator.signin_Teacher.module.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import static com.example.administrator.signin_Teacher.view.MainActivity.mList;

public class NotArriveActivity extends AppCompatActivity {
    int mYear, mMonth, mDay,mHour,mMin;

    long minTime,maxTime;
    final int DATE_DIALOG1 = 1;
    final int TIME_DIALOG1 = 2;
    final int DATE_DIALOG2 = 3;
    final int TIME_DIALOG2 = 4;
    private List<CountNotArrive> mCountNotList;
    private Button startbtn;
    private Button endbtn;
    private Button query;
    private Button countQuery;
    private TextView startText;
    private TextView endText;
    private RecyclerView recycler;
    private android.widget.Spinner spinner;
    private android.widget.LinearLayout activitynotarrive;
    private String cId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_arrive);
        this.activitynotarrive = (LinearLayout) findViewById(R.id.activity_not_arrive);
        this.spinner = (Spinner) findViewById(R.id.select_course);
        this.recycler = (RecyclerView) findViewById(R.id.recycler);
        this.endText = (TextView) findViewById(R.id.endText);
        this.startText = (TextView) findViewById(R.id.startText);
        this.query = (Button) findViewById(R.id.query);
        this.endbtn = (Button) findViewById(R.id.endbtn);
        this.startbtn = (Button) findViewById(R.id.startbtn);
        this.countQuery = (Button) findViewById(R.id.count_query);
        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG1);
            }
        });
        endbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG2);

            }
        });
        ArrayList<String> strList = new ArrayList<String>();
        for (int i = 0; i < mList.size(); i++) {
            strList.add(mList.get(i).getCourseName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(NotArriveActivity.this, R.layout.my_spinner,R.id.text, strList);
        adapter.setDropDownViewResource(R.layout.my_spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cId = mList.get(position).getObjectId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);

        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = BmobUser.getCurrentUser(NotArriveActivity.this,User.class);
                BmobQuery<NotArrive> query1 = new BmobQuery<NotArrive>();
                query1.addWhereLessThanOrEqualTo("Time",maxTime);
                query1.addWhereEqualTo("cId",cId);
//返回50条数据，如果不加上这条语句，默认返回10条数据
                query1.setLimit(1000);
                BmobQuery<NotArrive> query2 = new BmobQuery<NotArrive>();
                query2.addWhereGreaterThanOrEqualTo("Time",minTime);
                List<BmobQuery<NotArrive>> queries = new ArrayList<BmobQuery<NotArrive>>();
                queries.add(query1);
                queries.add(query2);
                BmobQuery<NotArrive> query = new BmobQuery<NotArrive>();
                query.and(queries);
//执行查询方法
                query.findObjects(NotArriveActivity.this, new FindListener<NotArrive>() {
                    @Override
                    public void onSuccess(List<NotArrive> object) {
                        // TODO Auto-generated method stub

                        if (object.size() == 0) {
                            Toast.makeText(NotArriveActivity.this, "无记录", Toast.LENGTH_SHORT).show();
                        } else {
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
                            LinearLayoutManager manager = new LinearLayoutManager(NotArriveActivity.this);
                            recyclerView.setLayoutManager(manager);
                            NotArriveAdapter adapter = new NotArriveAdapter(object);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                    @Override
                    public void onError(int code, String msg) {
                        // TODO Auto-generated method stub
                        Toast.makeText(NotArriveActivity.this,"查询失败："+msg,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        countQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initStudentData(cId);
                BmobQuery<NotArrive> query1 = new BmobQuery<NotArrive>();
                query1.addWhereLessThanOrEqualTo("Time",maxTime);
                query1.addWhereEqualTo("cId",cId);
//返回50条数据，如果不加上这条语句，默认返回10条数据
                query1.setLimit(1000);
                BmobQuery<NotArrive> query2 = new BmobQuery<NotArrive>();
                query2.addWhereGreaterThanOrEqualTo("Time",minTime);
                List<BmobQuery<NotArrive>> queries = new ArrayList<BmobQuery<NotArrive>>();
                queries.add(query1);
                queries.add(query2);
                BmobQuery<NotArrive> query = new BmobQuery<NotArrive>();
                query.and(queries);
//执行查询方法
                query.findObjects(NotArriveActivity.this, new FindListener<NotArrive>() {
                    @Override
                    public void onSuccess(List<NotArrive> object) {
                        // TODO Auto-generated method stub

                        if (object.size() == 0) {
                            Toast.makeText(NotArriveActivity.this, "无记录", Toast.LENGTH_SHORT).show();
                        } else {
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
                            LinearLayoutManager manager = new LinearLayoutManager(NotArriveActivity.this);
                            recyclerView.setLayoutManager(manager);
                            for (NotArrive notArrive:object){
                                for (int i = 0; i < mCountNotList.size(); i++){
                                    if (notArrive.getId().equals(mCountNotList.get(i).getsId())){
                                        int temp = mCountNotList.get(i).getNotCount();
                                        temp++;
                                        mCountNotList.get(i).setNotCount(temp);
                                    }
                                }
                            }
                            CountNotArriveAdapter adapter = new CountNotArriveAdapter(mCountNotList);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                    @Override
                    public void onError(int code, String msg) {
                        // TODO Auto-generated method stub
                        Toast.makeText(NotArriveActivity.this,"查询失败："+msg,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG1:
                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
            case TIME_DIALOG1:
                return new TimePickerDialog(this,mtimeListener,mHour,mMin,true);
            case DATE_DIALOG2:
                return new DatePickerDialog(this, mdate2Listener, mYear, mMonth, mDay);
            case TIME_DIALOG2:
                return new TimePickerDialog(this,mtime2Listener,mHour,mMin,true);
        }
        return null;
    }


    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            showDialog(TIME_DIALOG1);

        }
    };
    private TimePickerDialog.OnTimeSetListener mtimeListener = new TimePickerDialog.OnTimeSetListener(){

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mHour = hourOfDay;
            mMin = minute;
            Date date = new Date(mYear-1900,mMonth,mDay,mHour,mMin);
            minTime = date.getTime();
            SimpleDateFormat formatter    =   new SimpleDateFormat("yyyy年MM月dd日\nHH:mm:ss");
            Date indata = new Date(minTime);//获取当前时间
            startText.setText(formatter.format(indata));
        }
    };
    private DatePickerDialog.OnDateSetListener mdate2Listener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            showDialog(TIME_DIALOG2);

        }
    };
    private TimePickerDialog.OnTimeSetListener mtime2Listener = new TimePickerDialog.OnTimeSetListener(){

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mHour = hourOfDay;
            mMin = minute;
            Date date = new Date(mYear-1900,mMonth,mDay,mHour,mMin);
            maxTime = date.getTime();
            SimpleDateFormat formatter    =   new SimpleDateFormat("yyyy年MM月dd日\nHH:mm:ss");
            Date indata = new Date(maxTime);//获取当前时间
            endText.setText(formatter.format(indata));
        }
    };
    private void initStudentData(String cId){
        BmobQuery<StudentCourse> query = new BmobQuery<>();
        query.addWhereEqualTo("cId",cId);
        query.findObjects(NotArriveActivity.this, new FindListener<StudentCourse>() {
            @Override
            public void onSuccess(List<StudentCourse> list) {
                mCountNotList = new ArrayList<CountNotArrive>();
                for (StudentCourse li:list) {
                    CountNotArrive countNotArrive = new CountNotArrive();
                    countNotArrive.setcId(li.getcId());
                    countNotArrive.setName(li.getName());
                    countNotArrive.setsId(li.getsId());
                    countNotArrive.setNotCount(0);
                    mCountNotList.add(countNotArrive);
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
}
