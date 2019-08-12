package com.Dream.service.impl;

import com.Dream.dao.UploadFileDao;
import com.Dream.entity.UploadFile;
import com.Dream.service.UploadFileListService;
import com.Dream.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class UploadFileListServiceImpl implements UploadFileListService {

    @Autowired
    private UploadFileService uploadFileService;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int insertList(List<UploadFile> list) {
        int count = 0;
        for(UploadFile file:list){
            count +=uploadFileService.uploadFile(file);
        }
        return count;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int insertMap(Map<String, UploadFile> map) {
        int count = 0;
        for(Map.Entry<String, UploadFile> entry: map.entrySet()){
            UploadFile file = entry.getValue();
            count += uploadFileService.uploadFile(file);
        }
        return count;
    }
}
