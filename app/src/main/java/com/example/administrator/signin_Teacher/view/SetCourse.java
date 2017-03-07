package com.example.administrator.signin_Teacher.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.signin_Teacher.R;
import com.example.administrator.signin_Teacher.adapter.CourseAdapter;
import com.example.administrator.signin_Teacher.module.Course;
import com.example.administrator.signin_Teacher.module.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import static com.example.administrator.signin_Teacher.view.MainActivity.mList;

public class SetCourse extends AppCompatActivity {

    private RecyclerView setCourse;

    private CourseAdapter adapter;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        user = BmobUser.getCurrentUser(SetCourse.this,User.class);
        setCourse = (RecyclerView) findViewById(R.id.setCourse);
        LinearLayoutManager manager = new LinearLayoutManager(SetCourse.this);
        setCourse.setLayoutManager(manager);
        adapter = new CourseAdapter(mList);
        setCourse.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.course_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.addCourse) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SetCourse.this);
            LayoutInflater layoutInflater = LayoutInflater.from(SetCourse.this);
            final View conView = layoutInflater.inflate(R.layout.new_course_item, null);
            final EditText editText = (EditText) conView.findViewById(R.id.courseName);
            builder.setView(conView);
            builder.setTitle("请输入课程名称");
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Course course = new Course();
                        int teacher = Integer.valueOf(user.getUsername());
                        String courseName = editText.getText().toString();
                        course.setCourseName(courseName);
                        course.setTeacherName(user.getName());
                        course.setId(Integer.valueOf(user.getUsername()));
                        course.setcId(course.getObjectId());
                        BmobQuery<Course> query1 = new BmobQuery<Course>();
                        query1.addWhereEqualTo("courseName",courseName);
                        query1.addWhereEqualTo("Id",teacher);
                        query1.findObjects(SetCourse.this, new FindListener<Course>() {
                            @Override
                            public void onSuccess(List<Course> list) {
                                if (list.isEmpty()){
                                    course.save(SetCourse.this, new SaveListener() {
                                        @Override
                                        public void onSuccess() {
                                            Toast.makeText(SetCourse.this, "添加课程成功！", Toast.LENGTH_SHORT).show();
                                            mList.add(course);
                                            adapter.notifyItemInserted(mList.size() - 1);
                                        }

                                        @Override
                                        public void onFailure(int i, String s) {
                                            Toast.makeText(SetCourse.this, "添加课程失败！", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else {
                                    Toast.makeText(SetCourse.this, "课程已经存在", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(int i, String s) {

                            }
                        });
                    }
                }).start();
                    }
            });
            builder.show();
        }else if (id == R.id.deleteCourse){
            final ArrayList<Boolean> flag = adapter.getFlag();
            for (int i = flag.size() - 1; i >= 0; i--) {
                if (flag.get(i)){
                    Course course = mList.get(i);
                    final int finalI = i;
                    course.delete(SetCourse.this, new DeleteListener() {
                        @Override
                        public void onSuccess() {
                            mList.remove(finalI);
                            flag.remove(finalI);
                            adapter.setFlag(flag);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(SetCourse.this, "删除失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }



        }
        return true;
    }

}
