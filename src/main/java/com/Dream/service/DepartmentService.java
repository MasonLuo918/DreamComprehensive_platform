package com.Dream.service;

import com.Dream.entity.Department;

public interface DepartmentService {

    /**
     * 判断允不允许注册，如果email在数据库中有相同的，则不允许注册
     * 不允许对参数进行修改
     * 一个学院的一个部门只允许注册一次
     * @param department
     * @return
     */
    boolean allowRegister(Department department);

    /**
     * 插入一个新的部门
     * @param department
     * @return
     */
    int insertDepartment(Department department);

    /**
     * 删除一个部门
     * @param email
     * @return
     */
    int deleteDepartment(String email);

}
