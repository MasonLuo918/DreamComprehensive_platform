package com.Dream.dao;

import com.Dream.entity.UploadFile;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadFileDao{
    int insert(UploadFile uploadFile);

    int delete(String uuid);

    UploadFile selectByUuid(String uuid);
}
