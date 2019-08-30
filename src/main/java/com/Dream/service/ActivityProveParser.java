package com.Dream.service;

import com.Dream.dao.UploadFileDao;
import com.Dream.entity.ActivityProve;
import com.Dream.entity.UploadFile;
import com.Dream.entity.type.FileType;
import com.Dream.service.ActivityProveListService;
import com.Dream.util.compress.ZipUtils;
import com.Dream.util.parse.docparse.DocParser;
import com.Dream.util.parse.docparse.WordDocParser;
import com.Dream.util.parse.docparse.WordDocxParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")  //每次都创建一个示例
public class ActivityProveParser implements Runnable {

    private UploadFile uploadFile;

    private int fileType;

    private Integer activityID;

    @Autowired
    private ActivityProveListService activityProveListService;

    @Autowired
    private UploadFileDao uploadFileDao;


    public boolean decompress(Charset charset) {
        if (uploadFile == null || uploadFile.getPath() == null || !uploadFile.getPath().endsWith("zip")) {
            return false;
        }
        File srcFile = new File(uploadFile.getPath());
        String destPath = srcFile.getParent() + File.separator + uploadFile.getUuid();
        try {
            ZipUtils.decompress(srcFile, destPath, charset);
            uploadFile.setPath(uploadFile.getPath() + ";" + destPath);
            uploadFileDao.update(uploadFile);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int parseDocument() {
        File file = new File(uploadFile.getPath());
        DocParser docParser = null;
        String fileName = file.getName();
        if (fileName.endsWith(".doc")) {
            docParser = new WordDocParser();
        } else {
            docParser = new WordDocxParser();
        }
        List<ActivityProve> lists = docParser.getResult(uploadFile.getPath(), activityID);
        return activityProveListService.insertList(lists);
    }

    @Override
    public void run() {
        switch (fileType) {
            case FileType.MATERIAL:
                if(!decompress(null)){
                    decompress(Charset.forName("GBK"));
                };
                break;
            case FileType.ACTIVITY_PROVE_DOC:
                parseDocument();
                break;
            case FileType.VOLUNTEER_DOC:
                parseDocument();
                break;
        }
    }

    public UploadFile getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(UploadFile uploadFile) {
        this.uploadFile = uploadFile;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public Integer getActivityID() {
        return activityID;
    }

    public void setActivityID(Integer activityID) {
        this.activityID = activityID;
    }
}
