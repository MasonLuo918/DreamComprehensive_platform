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
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    public int insertDepartment(Department department) {
       return departmentDao.insert(department);
    }

    @Override
    public int deleteDepartment(String eamil) {
        return departmentDao.deleteByEmail(eamil);
    }

    @Override
    public Department findByEmail(String email) {
        return departmentDao.selectByEmail(email);
    }

    @Override
    public Department findByCollegeAndDeptName(String college, String deptName) {
        Department department = new Department();
        department.setCollege(college);
        department.setDeptName(deptName);
        return departmentDao.selectOne(department);
    }

    @Override
    public int update(Department department) {
        return departmentDao.update(department);
    }

    public DepartmentDao getDepartmentDao() {
        return departmentDao;
    }

    public void setDepartmentDao(DepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }
}
