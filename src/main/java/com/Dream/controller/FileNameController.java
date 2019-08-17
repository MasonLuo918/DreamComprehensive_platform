package com.Dream.controller;

import com.Dream.entity.UploadFile;
import com.Dream.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Map;

@RequestMapping("/admin/file")
@Controller
public class FileNameController {

    @Autowired
    private UploadFileService uploadFileService;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public UploadFile getFileDetail(@RequestBody Map<String, String> map){
        String uuid = (String) map.get("uuid");
        return uploadFileService.findByUuid(uuid);
    }
}
