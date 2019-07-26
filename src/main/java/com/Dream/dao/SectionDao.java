package com.Dream.dao;

import com.Dream.entity.Section;

import java.util.List;

public interface SectionDao extends BaseDao<Section> {

    /**
     * 根据组织id查找部门
     * @param departmentId
     * @return
     */
    List<Section> selectByDepartmentId(Integer departmentId);

    /**
     * 根据账户查找部门
     * @param account
     * @return
     */
    Section selectByAccount(String account);

    /**
     * 根据账户删除部门
     * @param account
     * @return
     */
    int deleteByAccount(String account);
}
