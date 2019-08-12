package com.Dream.service;

import com.Dream.entity.UploadFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadFileService {
    int uploadFile(UploadFile uploadFile);

    int deleteFile(String uuid);

    UploadFile findByUuid(String uuid);

    /**
     * 将文件传输到指定的路径上, 但不进存入数据库
     * @param parentPath
     * @param file
     * @return
     * @throws IOException
     */
    UploadFile uploadSimpleFile(String parentPath, MultipartFile file, int activityID) throws IOException;
}
