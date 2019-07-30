package com.Dream.dao;

import com.Dream.entity.Section;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionDao extends BaseDao<Section> {

    /**
     * 根据组织id查找部门
     * @param departmentID
     * @return
     */
    List<Section> selectByDepartmentID(Integer departmentID);

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
