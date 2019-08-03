package com.Dream.entity;

public class ActivityProve {
    private Integer id;

    private Integer activityID;

    private Double volunTimeNum;

    // 1 代表志愿时，0代表活动分
    private Integer type;

    private String stuNum;

    private String stuClass;

    private String stuName;

    private Activity activity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getActivityId() {
        return activityID;
    }

    public void setActivityId(Integer activityId) {
        this.activityID = activityId;
    }

    public String getStuClass() {
        return stuClass;
    }

    public void setStuClass(String stuClass) {
        this.stuClass = stuClass;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public Activity getActivity() {
        return activity;
    }

    public Double getVolunTimeNum() {
        return volunTimeNum;
    }

    public void setVolunTimeNum(Double volunTimeNum) {
        this.volunTimeNum = volunTimeNum;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Integer getActivityID() {
        return activityID;
    }

    public void setActivityID(Integer activityID) {
        this.activityID = activityID;
    }

    public String getStuNum() {
        return stuNum;
    }

    public void setStuNum(String stuNum) {
        this.stuNum = stuNum;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
