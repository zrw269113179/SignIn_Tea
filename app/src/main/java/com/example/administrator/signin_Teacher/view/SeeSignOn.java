package com.example.administrator.signin_Teacher.view;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.signin_Teacher.R;
import com.example.administrator.signin_Teacher.adapter.UnSignOnAdapter;
import com.example.administrator.signin_Teacher.module.GeoPoint;
import com.example.administrator.signin_Teacher.module.NotArrive;
import com.example.administrator.signin_Teacher.module.Record;
import com.example.administrator.signin_Teacher.module.StudentCourse;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import static com.example.administrator.signin_Teacher.view.MainActivity.point;
import static com.example.administrator.signin_Teacher.view.MainActivity.user;

public class SeeSignOn extends AppCompatActivity {

    private List<Record> mList;
    private List<StudentCourse> mStudentList;
    private TextView signOnCount;
    private RecyclerView mRecycler;
    private Button endsignin;
    public static List<StudentCourse> mUnSignOn;
    private String cName;
    private UnSignOnAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_sign_on);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        signOnCount = (TextView)findViewById(R.id.sign_on_count);
        mRecycler = (RecyclerView)findViewById(R.id.sign_on_person);
        this.endsignin = (Button) findViewById(R.id.end_sign_in);
        setSupportActionBar(toolbar);
        LinearLayoutManager manager = new LinearLayoutManager(SeeSignOn.this);
        mRecycler.setLayoutManager(manager);
        mUnSignOn = new ArrayList<StudentCourse>();
        adapter = new UnSignOnAdapter(mUnSignOn);
        mRecycler.setAdapter(adapter);

        cName = getSharedPreferences("data",MODE_PRIVATE).getString("cName","");

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.mRefresh);
        //设置刷新时动画的颜色，可以设置4个
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (point == null) {
                    Toast.makeText(SeeSignOn.this, "还未发起签到", Toast.LENGTH_SHORT).show();
                }else {
                    initData();
                }
            }
        });
        //结束签到
        endsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobQuery<GeoPoint> geoQuery = new BmobQuery<GeoPoint>();
                geoQuery.addWhereEqualTo("tId",Integer.valueOf(user.getUsername()));
                geoQuery.findObjects(SeeSignOn.this, new FindListener<GeoPoint>() {
                    @Override
                    public void onSuccess(List<GeoPoint> list) {
                        if (list.isEmpty()){
                            Toast.makeText(SeeSignOn.this, "未发起签到", Toast.LENGTH_SHORT).show();
                        }else {
                            if (mUnSignOn == null){
                                Toast.makeText(SeeSignOn.this, "请先查看签到情况", Toast.LENGTH_SHORT).show();
                            }else{
                                for (StudentCourse stu:mUnSignOn){
                                    NotArrive not = new NotArrive();
                                    not.setName(stu.getName());
                                    not.setId(stu.getsId());
                                    not.setTime(System.currentTimeMillis());
                                    not.setcId(stu.getcId());
                                    not.setcName(cName);
                                    not.save(SeeSignOn.this, new SaveListener() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onFailure(int i, String s) {

                                        }
                                    });
                                }
                            }
                            point = list.get(0);
                            point.delete(SeeSignOn.this, new DeleteListener() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(SeeSignOn.this, "已结束签到！", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(int i, String s) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });
            }
        });


        if (point == null) {
            Toast.makeText(this, "还未发起签到", Toast.LENGTH_SHORT).show();
        }else {
            initStudentData();
            initData();

        }
    }
    public void initData() {

            BmobQuery<Record> query = new BmobQuery<>();
            query.addWhereEqualTo("cId", point.getcId());
            query.addWhereGreaterThanOrEqualTo("in_time", point.getTime());
            query.findObjects(SeeSignOn.this, new FindListener<Record>() {
                @Override
                public void onSuccess(List<Record> list) {
                    mList = list;
                    for (int i = 0; i < mUnSignOn.size(); i++) {
                        mUnSignOn.remove(i);
                    }
                    //mUnSignOn = new ArrayList<StudentCourse>();
                    swipeRefreshLayout.setRefreshing(false);
                    signOnCount.setText("已签到：" + list.size() + "/" + mStudentList.size());
                    for (int i = 0; i < mStudentList.size(); i++) {
                        boolean flag = false;
                        for (Record record:list) {
                            if (mStudentList.get(i).getsId() == record.getID()){
                                flag = true;
                            }
                        }
                        if (!flag){
                            mUnSignOn.add(mStudentList.get(i));
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onError(int i, String s) {

                }
            });
    }
    private void initStudentData(){
        BmobQuery<StudentCourse> query = new BmobQuery<>();
        query.addWhereEqualTo("cId",point.getcId());
        query.findObjects(SeeSignOn.this, new FindListener<StudentCourse>() {
            @Override
            public void onSuccess(List<StudentCourse> list) {
                mStudentList = list;
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

}
