package com.Dream.service;

import com.Dream.entity.UploadFile;

import java.util.List;
import java.util.Map;

public interface UploadFileListService {
    int insertList(List<UploadFile> list);

    int insertMap(Map<String, UploadFile> map);
}
