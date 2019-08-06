package com.Dream.dao;

import com.Dream.entity.UploadFile;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadFileDao{
    int insert(UploadFile uploadFile);

    int delete(String uuid);

    int update(UploadFile uploadFile);

    UploadFile selectByUuid(String uuid);
}
