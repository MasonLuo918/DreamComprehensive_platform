package com.Dream.entity;

import java.time.LocalDate;

public class Department {
    private Integer id;

    private String email;

    private String password;

    private String deptName;

    private String college;

    //0代表不可用状态
    private Integer status;

    private LocalDate createTime;


    public Department() {
        createTime = LocalDate.now();
    }


    public Department(String email, String password, String deptName, String college, Integer status, LocalDate createTime) {
        this.email = email;
        this.password = password;
        this.deptName = deptName;
        this.college = college;
        this.status = status;
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDate getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDate createTime) {
        this.createTime = createTime;
    }
}
