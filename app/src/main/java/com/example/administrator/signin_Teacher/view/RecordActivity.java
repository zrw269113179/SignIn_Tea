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
import com.example.administrator.signin_Teacher.adapter.RecordAdapter;
import com.example.administrator.signin_Teacher.module.Record;
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


public class RecordActivity extends AppCompatActivity {
    int mYear, mMonth, mDay,mHour,mMin;

    long minTime,maxTime;
    final int DATE_DIALOG1 = 1;
    final int TIME_DIALOG1 = 2;
    final int DATE_DIALOG2 = 3;
    final int TIME_DIALOG2 = 4;
    private Button startbtn;
    private Button endbtn;
    private Button query;
    private TextView startText;
    private TextView endText;
    private String cId;
    private RecyclerView recycler;
    private android.widget.LinearLayout activityrecord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);
        Spinner spinner = (Spinner) findViewById(R.id.select_course);
        TextView endText = (TextView) findViewById(R.id.endText);
        TextView startText = (TextView) findViewById(R.id.startText);
        Button endbtn = (Button) findViewById(R.id.endbtn);
        Button startbtn = (Button) findViewById(R.id.startbtn);
        this.activityrecord = (LinearLayout) findViewById(R.id.activity_record);
        this.recycler = (RecyclerView) findViewById(R.id.recycler);
        this.endText = (TextView) findViewById(R.id.endText);
        this.startText = (TextView) findViewById(R.id.startText);
        this.query = (Button) findViewById(R.id.query);
        this.endbtn = (Button) findViewById(R.id.endbtn);
        this.startbtn = (Button) findViewById(R.id.startbtn);
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RecordActivity.this, R.layout.my_spinner,R.id.text, strList);
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
                User user = BmobUser.getCurrentUser(RecordActivity.this,User.class);
                BmobQuery<Record> query1 = new BmobQuery<Record>();
                query1.addWhereLessThanOrEqualTo("in_time",maxTime);
                query1.addWhereEqualTo("cId",cId);
//返回1000条数据，如果不加上这条语句，默认返回10条数据
                query1.setLimit(1000);
                BmobQuery<Record> query2 = new BmobQuery<Record>();
                query2.addWhereGreaterThanOrEqualTo("in_time",minTime);
                List<BmobQuery<Record>> queries = new ArrayList<BmobQuery<Record>>();
                queries.add(query1);
                queries.add(query2);
                BmobQuery<Record> query = new BmobQuery<Record>();
                query.and(queries);
//执行查询方法
                query.findObjects(RecordActivity.this, new FindListener<Record>() {
                    @Override
                    public void onSuccess(List<Record> object) {
                        // TODO Auto-generated method stub
                        if (object.size() == 0) {
                            Toast.makeText(RecordActivity.this, "无记录", Toast.LENGTH_SHORT).show();
                        } else {
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
                            LinearLayoutManager manager = new LinearLayoutManager(RecordActivity.this);
                            recyclerView.setLayoutManager(manager);
                            RecordAdapter adapter = new RecordAdapter(object);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                    @Override
                    public void onError(int code, String msg) {
                        // TODO Auto-generated method stub
                        Toast.makeText(RecordActivity.this,"查询失败："+msg,Toast.LENGTH_SHORT).show();
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
}
