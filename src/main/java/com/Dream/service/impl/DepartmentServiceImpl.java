package com.Dream.service.impl;

import com.Dream.dao.DepartmentDao;
import com.Dream.entity.Department;
import com.Dream.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentDao departmentDao;

    @Override
    public boolean allowRegister(Department department) {
        /*
         * 根据学院和部门名称查询，如果已经存在，则不允许注册
         */
        Department queryDepartment = new Department();
        queryDepartment.setDeptName(department.getDeptName());
        queryDepartment.setCollege(department.getCollege());
        Department resultDepartment = departmentDao.selectOne(queryDepartment);
        if(resultDepartment != null){
            return false;
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    public int insertDepartment(Department department) {
       return departmentDao.insert(department);
    }

    @Override
    public int deleteDepartment(String eamil) {
        return departmentDao.deleteByEmail(eamil);
    }

}
