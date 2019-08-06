package com.Dream.service;

import com.Dream.entity.UploadFile;

public interface UploadFileService {
    int uploadFile(UploadFile uploadFile);

    int deleteFile(String uuid);

    UploadFile findByUuid(String uuid);
}
