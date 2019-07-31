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

}
