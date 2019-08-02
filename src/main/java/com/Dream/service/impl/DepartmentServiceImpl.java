package com.Dream.service.impl;

import com.Dream.dao.DepartmentDao;
import com.Dream.entity.Department;
import com.Dream.mail.SendEmail;
import com.Dream.service.DepartmentService;
import com.Dream.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private RedisTemplate redisTemplate;


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

    @Override
    public Department login(String email, String password) {
        Department department = new Department();
        department.setEmail(email);
        department.setPassword(MD5Util.getMD5(password));
        return departmentDao.selectOne(department);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public int register(Department department, String activateCode) {

        int count = departmentDao.insert(department);
        redisTemplate.opsForValue().set(department.getEmail(), activateCode);
        try {
            SendEmail.sendMail(department.getEmail(), activateCode, department.getDeptName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return count;
    }

}
