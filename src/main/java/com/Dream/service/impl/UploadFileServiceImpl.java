package com.Dream.service.impl;

import com.Dream.dao.UploadFileDao;
import com.Dream.entity.UploadFile;
import com.Dream.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UploadFileServiceImpl implements UploadFileService {

    @Autowired
    private UploadFileDao uploadFileDao;

    @Override
    public int uploadFile(UploadFile uploadFile) {
        return uploadFileDao.insert(uploadFile);
    }

    @Override
    public int deleteFile(String uuid) {
        return uploadFileDao.delete(uuid);
    }

    @Override
    public UploadFile findByUuid(String uuid) {
        return uploadFileDao.selectByUuid(uuid);
    }
}
