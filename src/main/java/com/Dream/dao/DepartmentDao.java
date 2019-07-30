package com.Dream.dao;

import com.Dream.entity.Department;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentDao extends BaseDao<Department> {
    /**
     * 根据组织名称查找组织（多个学院的用一组织）
     * @param deptName
     * @return
     */
    List<Department> selectByDepartmentName(String deptName);

    /**
     * 根据学院查找组织
     * @param college
     * @return
     */
    List<Department> selectByCollege(String college);

    /**
     * 根据邮箱*（账户）查找组织
     * @param email
     * @return
     */
    Department selectByEmail(String email);

    /**
     * 删除组织
     * @param email
     * @return
     */
    int deleteByEmail(String email);

}
