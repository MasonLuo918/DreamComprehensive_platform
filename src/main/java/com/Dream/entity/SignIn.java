package com.Dream.entity;

import java.io.Serializable;
import java.time.LocalDate;

public class SignIn implements Serializable {
    private Integer id;

    private String stuID;

    private String stuName;

    private String stuProfessionAndClass;

    private Integer activityID;

    private String note;

    private LocalDate createTime;


    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }


    public String getStuName() { return stuName; }

    public void setStuName(String stuName) { this.stuName = stuName; }

    public String getStuProfessionAndClass() { return stuProfessionAndClass; }

    public void setStuProfessionAndClass(String stuProfessionAndClass) { this.stuProfessionAndClass = stuProfessionAndClass; }

    public String getNote() { return note; }

    public void setNote(String note) { this.note = note; }

    public LocalDate getCreateTime() { return createTime; }

    public void setCreateTime(LocalDate createTime) { this.createTime = createTime; }

    public String getStuID() {
        return stuID;
    }

    public void setStuID(String stuID) {
        this.stuID = stuID;
    }

    public Integer getActivityID() {
        return activityID;
    }

    public void setActivityID(Integer activityID) {
        this.activityID = activityID;
    }
}
