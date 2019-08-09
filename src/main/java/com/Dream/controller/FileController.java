package com.Dream.controller;


import com.Dream.bean.Image;
import com.Dream.entity.Activity;
import com.Dream.entity.Department;
import com.Dream.entity.Section;
import com.Dream.entity.UploadFile;
import com.Dream.service.UploadFileService;
import com.Dream.util.GetFileList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/activity")
public class FileController {

    @Autowired
    private UploadFileService uploadFileService;

    /**
     * 返回对应uuid的文件里面的所有图片,以base64串进行返回
     * 请求参数:
     * {
     *     "uuid":"..."
     * }
     * 返回:
     * {
     *      "status":"",
     *      "images":[
     *          {"base64Str":"", "contentType":""},
     *          {"base64Str":"", "contentType":""},
     *          {"base64Str":"", "contentType":""}
     *      ]
     * }
     * @param requestMap
     * @return
     */
    @RequestMapping("/getZipContent") // 可以利用aop检查一下是否请求的文件uuid是该会话用户的，还有文档类型
    @ResponseBody
    public Map<String, Object> getZipFileContent(@RequestBody Map<String, Object> requestMap){
        String uuid = (String)requestMap.get("uuid");
        Map<String, Object> resultMap = new HashMap<>();
        if(uuid == null){
            return paramLost();
        }

        List<String> photoList = new ArrayList<>();
        UploadFile file = uploadFileService.findByUuid(uuid);
        if(file == null){
            resultMap.put("status","201");
            resultMap.put("message","uuid错误，文件不存在");
        }
        if(!file.getName().endsWith("zip")){
            resultMap.put("status","201");
            resultMap.put("message","uuid错误，非zip文件类型");
        }
        String[] paths = file.getPath().split(";");
        String path = paths[1];
        // 获取一个解析类
        GetFileList getFileList = new GetFileList();
        List<Image> base64List = getFileList.parseFile(path);
        resultMap.put("status","200");
        resultMap.put("images", base64List);
        return resultMap;
    }

    private Map<String, Object> paramLost(){
        Map<String, Object> map = new HashMap<>();
        map.put("status","002");
        map.put("message","请求参数丢失");
        return map;
    }
}
