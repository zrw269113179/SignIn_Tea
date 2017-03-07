package com.example.administrator.signin_Teacher.module;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017-02-27 .
 */

public class StudentCourse extends BmobObject {
    private String cId;
    private Integer sId;
    private String name;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public int getsId() {
        return sId;
    }

    public void setsId(int sId) {
        this.sId = sId;
    }
}
