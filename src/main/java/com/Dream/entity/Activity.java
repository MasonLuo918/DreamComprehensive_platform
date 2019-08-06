package com.Dream.entity;


import java.time.LocalDate;

public class Activity {

    private Integer id;

    private String name;

    private LocalDate time;

    private Integer departmentID;

    private Integer sectionID;

    // 活动证明材料的uuid
    private String material;

    // 志愿时文档uuid
    private String volunteerTime;

    // 活动证明文档uuid
    private String activityProve;

    private UploadFile materialFile;

    private UploadFile volunteerTimeFile;

    private UploadFile activityProveFile;

    private Department department;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

    public Integer getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(Integer departmentID) {
        this.departmentID = departmentID;
    }

    public Integer getSectionID() {
        return sectionID;
    }

    public void setSectionID(Integer sectionID) {
        this.sectionID = sectionID;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getVolunteerTime() {
        return volunteerTime;
    }

    public void setVolunteerTime(String volunteerTime) {
        this.volunteerTime = volunteerTime;
    }

    public String getActivityProve() {
        return activityProve;
    }

    public void setActivityProve(String activityProve) {
        this.activityProve = activityProve;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public UploadFile getMaterialFile() {
        return materialFile;
    }

    public void setMaterialFile(UploadFile materialFile) {
        this.materialFile = materialFile;
    }

    public UploadFile getVolunteerTimeFile() {
        return volunteerTimeFile;
    }

    public void setVolunteerTimeFile(UploadFile volunteerTimeFile) {
        this.volunteerTimeFile = volunteerTimeFile;
    }

    public UploadFile getActivityProveFile() {
        return activityProveFile;
    }

    public void setActivityProveFile(UploadFile activityProveFile) {
        this.activityProveFile = activityProveFile;
    }
}
