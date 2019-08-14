package com.Dream.service;

import com.Dream.entity.Section;

public interface SectionService {

    /**
     * 插入一个新的组织
     * @param section
     * @return
     */

    int insertSection(Section section);

    /**
     * 删除一个组织
     * @param account
     * @return
     */

    int deleteSection(String account);

    int update(Section section);

    Section findByAccount(String account);

    /**
     * 根据邮箱密码登陆
     * @param account
     * @param password
     * @return
     */
    Section login(String account,String password);

    int register(Section section,String activatecode)throws Exception;
}
