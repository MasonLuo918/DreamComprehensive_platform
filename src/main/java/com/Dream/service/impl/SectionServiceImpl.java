package com.Dream.service.impl;

import com.Dream.commons.mail.SendEmail;
import com.Dream.dao.SectionDao;
import com.Dream.entity.Section;
import com.Dream.service.SectionService;
import com.Dream.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SectionServiceImpl implements SectionService {

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,isolation = Isolation.REPEATABLE_READ)
    public int insertSection(Section section){ return sectionDao.insert(section);}

    @Override
    public int deleteSection(String account){ return sectionDao.deleteByAccount(account);}

    @Override
    public int update(Section section){ return sectionDao.update(section);}

    @Override
    public Section findByAccount(String account){ return sectionDao.selectByAccount(account);}

    @Override
    public Section findByDepartmentIdAndName(Integer departmentId,String name){
        Section section=new Section();
        section.setDepartmentID(departmentId);
        section.setName(name);
       return sectionDao.selectOne(section);
    }

    @Override
    public Section login(String account,String password){
        Section section=new Section();
        section.setAccount(account);
        section.setPassword(MD5Util.getMD5(password));
        return sectionDao.selectOne(section);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT)
    public int register(Section section,String activatecode){
        int count=sectionDao.insert(section);
        redisTemplate.opsForValue().set(section.getAccount(),activatecode);
        try{
            SendEmail.sendMail(section.getAccount(),activatecode,section.getName());
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        return count;
    }


}
