package com.example.administrator.signin_Teacher.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.administrator.signin_Teacher.R;
import com.example.administrator.signin_Teacher.module.Course;
import com.example.administrator.signin_Teacher.module.GeoPoint;
import com.example.administrator.signin_Teacher.module.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class MainActivity extends AppCompatActivity {

    private android.widget.Button record;
    private Button not;
    private Button startsignin;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private int id;
    private String cId;
    public static GeoPoint point;
    public static List<Course> mList;
    private android.widget.Button shownum;

    private Button setCourse;
    public static User user;
    private Button setuser;
    private android.widget.LinearLayout activitymain;

    private void initData(){
        BmobQuery<GeoPoint> query1 = new BmobQuery();
        query1.addWhereEqualTo("tId",Integer.valueOf(user.getUsername()));
        query1.findObjects(MainActivity.this, new FindListener<GeoPoint>() {
            @Override
            public void onSuccess(List<GeoPoint> list) {
                if (list.isEmpty()){

                }else {
                    point = list.get(0);
                    Toast.makeText(MainActivity.this, "有签到未结束", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
        mList = new ArrayList<>();
        BmobQuery<Course> query = new BmobQuery<>();
        query.addWhereEqualTo("Id",Integer.valueOf(user.getUsername()));
        query.findObjects(MainActivity.this, new FindListener<Course>() {
            @Override
            public void onSuccess(List<Course> list) {
                for (int i = 0; i < list.size(); i++) {
                    mList.add(list.get(i));
                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(MainActivity.this, "初始化失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(false);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setuser = (Button) findViewById(R.id.set_user);
        user = BmobUser.getCurrentUser(MainActivity.this,User.class);
        this.setCourse = (Button) findViewById(R.id.setCourse);

        this.shownum = (Button) findViewById(R.id.show_num);
        this.startsignin = (Button) findViewById(R.id.start_sign_in);
        this.not = (Button) findViewById(R.id.not);
        this.record = (Button) findViewById(R.id.record);
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        initData();
        initLocation();
        mLocationClient.start();
        shownum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SeeSignOn.class));
            }
        });
        setCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SetCourse.class));
            }
        });
        setuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,newPassword.class));
            }
        });
        //查看签到学生
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RecordActivity.class));
            }
        });
        //查看未签到学生
        not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NotArriveActivity.class));
            }
        });
        //发起签到
        startsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            BmobQuery<GeoPoint> geoQuery = new BmobQuery<GeoPoint>();
            geoQuery.addWhereEqualTo("tId",Integer.valueOf(user.getUsername()));
            geoQuery.findObjects(MainActivity.this, new FindListener<GeoPoint>() {
                @Override
                public void onSuccess(List<GeoPoint> list) {
                    if (list.isEmpty()){
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                        View conView = layoutInflater.inflate(R.layout.start_sign_in_layout, null);
                        final EditText editText = (EditText) conView.findViewById(R.id.start_sign_in_id);
                        final Spinner spinner = (Spinner) conView.findViewById(R.id.select_course);
                        ArrayList<String> strList = new ArrayList<String>();
                        for (int i = 0; i < mList.size(); i++) {
                            strList.add(mList.get(i).getCourseName());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.my_spinner,R.id.text, strList);
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
                        builder.setView(conView);
                        builder.setTitle("请输入4位签到码");
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            String str = editText.getText().toString();
                            if (str.length() != 4) {
                                Toast.makeText(MainActivity.this, "签到码没有4位", Toast.LENGTH_SHORT).show();
                            } else {
                                if (cId == null) {
                                    Toast.makeText(MainActivity.this, "未选择课程", Toast.LENGTH_SHORT).show();
                                } else {
                                    id = Integer.valueOf(str);

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                        BmobQuery<GeoPoint> query = new BmobQuery<GeoPoint>();
                                        query.addWhereEqualTo("ID", id);
                                        query.findObjects(MainActivity.this, new FindListener<GeoPoint>() {
                                            @Override
                                            public void onSuccess(List<GeoPoint> list) {
                                                if (list.isEmpty()) {
                                                    point = new GeoPoint();
                                                    point.setID(id);
                                                    point.setcId(cId);
                                                    point.settId(Integer.valueOf(user.getUsername()));
                                                    point.setTime(System.currentTimeMillis());
                                                    point.save(MainActivity.this, new SaveListener() {
                                                        @Override
                                                        public void onSuccess() {
                                                            Toast.makeText(MainActivity.this, "发起签到成功", Toast.LENGTH_SHORT).show();
                                                            getSharedPreferences("data", MODE_PRIVATE).edit().putBoolean("signFlag", true).apply();
                                                            getSharedPreferences("data", MODE_PRIVATE).edit().putInt("signId", id).apply();
                                                            getSharedPreferences("data", MODE_PRIVATE).edit().putString("signObjectId", point.getObjectId()).apply();
                                                        }

                                                        @Override
                                                        public void onFailure(int i, String s) {

                                                        }
                                                    });
                                                } else {
                                                    Toast.makeText(MainActivity.this, "该签到码已经被使用！", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onError(int i, String s) {
                                            }
                                        });
                                        }
                                    }).start();
                                }
                            }
                            }
                        });
                        builder.show();
                    }else {
                        Toast.makeText(MainActivity.this, "尚有签到未结束，请先结束!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(int i, String s) {

                }
            });
            }
        });

    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            if (point != null) {
                point.setLatitude(location.getLatitude());
                point.setLongitude(location.getLongitude());
                point.update(MainActivity.this, point.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
            }
        }
    }
}
