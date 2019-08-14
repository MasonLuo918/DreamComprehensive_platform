package com.Dream.service.impl;

import com.Dream.dao.UploadFileDao;
import com.Dream.entity.UploadFile;
import com.Dream.entity.type.FileType;
import com.Dream.service.ActivityProveParser;
import com.Dream.service.UploadFileService;
import com.Dream.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;

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


    /**
     * 将文档提交，并且将该文档的uploadfile保存到数据库中，同时进行解析
     * @param parentPath
     * @param file
     * @param activityID
     * @return
     * @throws IOException
     */
    @Override
    public UploadFile uploadSimpleFile(String parentPath, MultipartFile file, int activityID) throws IOException {
        // 获取当前的容器
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        ActivityProveParser parser = webApplicationContext.getBean(ActivityProveParser.class);
        String fileName = file.getOriginalFilename();
        // 生成一个上传文件实例
        UploadFile uploadFile = new UploadFile();
        uploadFile.setUuid(UUIDUtils.getUUID());
        uploadFile.setName(fileName);
        // 将文件名重新命名为原名+uuid
        File destFile = new File(parentPath, uploadFile.getUuid() + " "  + fileName);
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        file.transferTo(destFile);
        uploadFile.setPath(destFile.getPath());
        uploadFileDao.insert(uploadFile);

        // 解析器设置解析的文件
        parser.setUploadFile(uploadFile);
        parser.setActivityID(activityID);
        if(file.getOriginalFilename().endsWith("zip")){
            parser.setFileType(FileType.MATERIAL);
        }else{
            parser.setFileType(FileType.VOLUNTEER_DOC); //设置活动证明类型都行
        }
        Thread thread = new Thread(parser);
        thread.start();
        return uploadFile;
    }

}
