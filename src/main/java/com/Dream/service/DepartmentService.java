package com.Dream.service;

import com.Dream.entity.Department;

public interface DepartmentService {


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

    Department findByEmail(String email);

    Department findByCollegeAndDeptName(String college, String deptName);

    int update(Department department);

    /**
     * 根据邮箱和密码登录,密码未用md5加密
     * @param email
     * @param password
     * @return
     */
    Department login(String email, String password);

    /**
     * 插入一个department，并且在redis中存放验证信息，
     * 发送邮件
     * 使用事务，一个出错，就回滚
     * @param department
     * @param activateCode
     * @return
     * @throws Exception
     */
    int register(Department department, String activateCode) throws Exception;
}
