package com.Dream.controller;

import com.Dream.dao.UploadFileDao;
import com.Dream.entity.ActivityProve;
import com.Dream.entity.UploadFile;
import com.Dream.service.ActivityProveListService;
import com.Dream.util.compress.ZipUtils;
import com.Dream.util.parse.docparse.DocParser;
import com.Dream.util.parse.docparse.WordDocParser;
import com.Dream.util.parse.docparse.WordDocxParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class ActivityProveParser implements Runnable{

    private UploadFile zipFile;

    private List<UploadFile> docList = new ArrayList<>();

    private Integer activityID;

    @Autowired
    private ActivityProveListService activityProveListService;

    @Autowired
    private UploadFileDao uploadFileDao;

    public boolean decompress(UploadFile zipFile){
        if(zipFile == null || zipFile.getPath() == null || !zipFile.getPath().endsWith("zip")){
            return false;
        }
        File srcFile = new File(zipFile.getPath());
        String destPath = srcFile.getParent() + File.separator + zipFile.getUuid();
        try {
            ZipUtils.decompress(srcFile, destPath);
            zipFile.setPath(zipFile.getPath() + ";" + destPath);
            uploadFileDao.update(zipFile);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int parseDocument(UploadFile uploadFile){
        File file = new File(uploadFile.getPath());
        DocParser docParser = null;
        String fileName = file.getName();
        if(fileName.endsWith(".doc")){
            docParser = new WordDocParser();
        }else{
            docParser = new WordDocxParser();
        }
        List<ActivityProve> lists = docParser.getResult(uploadFile.getPath(), activityID);
        return activityProveListService.insertList(lists);
    }

    @Override
    public void run() {
        if(zipFile != null){
            decompress(zipFile);
        }
        if(docList.size() != 0){
            for(UploadFile file:docList){
                parseDocument(file);
            }
        }
    }

    public UploadFile getZipFile() {
        return zipFile;
    }

    public void setZipFile(UploadFile zipFile) {
        this.zipFile = zipFile;
    }

    public List<UploadFile> getDocList() {
        return docList;
    }

    public void setDocList(List<UploadFile> docList) {
        this.docList = docList;
    }

    public Integer getActivityID() {
        return activityID;
    }

    public void setActivityID(Integer activityID) {
        this.activityID = activityID;
    }
}
