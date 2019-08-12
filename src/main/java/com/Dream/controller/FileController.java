package com.Dream.controller;


import com.Dream.bean.Image;
import com.Dream.entity.*;
import com.Dream.entity.type.FileType;
import com.Dream.service.ActivityProveService;
import com.Dream.service.ActivityService;
import com.Dream.service.UploadFileService;
import com.Dream.util.GetFileList;
import com.Dream.util.ParamUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/admin/activity")
public class FileController {

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityProveService activityProveService;

    /**
     * 返回对应uuid的文件里面的所有图片,以base64串进行返回
     * 请求参数:
     * {
     * "uuid":"..."
     * }
     * 返回:
     * {
     * "status":"",
     * "images":[
     * {"base64Str":"", "contentType":""},
     * {"base64Str":"", "contentType":""},
     * {"base64Str":"", "contentType":""}
     * ]
     * }
     *
     * @param requestMap
     * @return
     */
    @RequestMapping("/getZipContent") // 可以利用aop检查一下是否请求的文件uuid是该会话用户的，还有文档类型
    @ResponseBody
    public Map<String, Object> getZipFileContent(@RequestBody Map<String, Object> requestMap) {
        String uuid = (String) requestMap.get("uuid");
        Map<String, Object> resultMap = new HashMap<>();
        if (uuid == null) {
            return paramLost();
        }

        List<String> photoList = new ArrayList<>();
        UploadFile file = uploadFileService.findByUuid(uuid);
        if (file == null) {
            resultMap.put("status", "201");
            resultMap.put("message", "uuid错误，文件不存在");
        }
        if (!file.getName().endsWith("zip")) {
            resultMap.put("status", "201");
            resultMap.put("message", "uuid错误，非zip文件类型");
        }
        String[] paths = file.getPath().split(";");
        String path = paths[1];
        // 获取一个解析类
        GetFileList getFileList = new GetFileList();
        List<Image> base64List = getFileList.parseFile(path);
        resultMap.put("status", "200");
        resultMap.put("images", base64List);
        return resultMap;
    }

    /**
     * 获取改活动上传了的文档解析出来的数据
     * 所需要参数：
     * uuid:需要查看文档的uuid
     * file_type:文档的类型，1代表志愿时公示文档，0代表活动分文档
     * activity_id:所需要查询的活动id
     * @param requestMap
     * @return
     * {
     *     "parse_data": [
     *         {
     *             "id": 862,
     *             "activityID": 102,
     *             "volunTimeNum": 6.0,
     *             "type": 1,
     *             "stuNum": "201625010417",
     *             "stuClass": "16计机四班",
     *             "stuName": "林晓乙",
     *             "activityId": 102
     *         },
     *         {
     *             "id": 864,
     *             "activityID": 102,
     *             "volunTimeNum": 6.0,
     *             "type": 1,
     *             "stuNum": "201525050121",
     *             "stuClass": "15网工一班",
     *             "stuName": "吕丹婷",
     *             "activityId": 102
     *         },
     *         {
     *             "id": 866,
     *             "activityID": 102,
     *             "volunTimeNum": 6.0,
     *             "type": 1,
     *             "stuNum": "201525040124",
     *             "stuClass": "15信管一班",
     *             "stuName": "余浩璇",
     *             "activityId": 102
     *         }
     *     ],
     *     "status": "200"
     * }
     */
    @RequestMapping("/getDocParseData")
    @ResponseBody
    public Map<String, Object> getDocParseData(@RequestBody Map<String, Object> requestMap) {
        String uuid = (String) requestMap.get("uuid");
        int fileType = (int) requestMap.get("file_type");
        int activityID = (int) requestMap.get("activity_id");
        Map<String, Object> resultMap = new HashMap<>();
        if (ParamUtil.hasNull(uuid, fileType, activityID)) {
            return paramLost();
        }
        UploadFile file = uploadFileService.findByUuid(uuid);
        if (file == null) {
            resultMap.put("status", "201");
            resultMap.put("message", "uuid错误，文件不存在");
            return resultMap;
        }
        if (!file.getName().endsWith("doc") && !file.getName().endsWith("docx")) {
            resultMap.put("status", "201");
            resultMap.put("message", "uuid错误，非doc文档类型文件");
            return resultMap;
        }

        Activity activity = activityService.findByID(activityID);
        if (activity == null) {
            resultMap.put("status", "201");
            resultMap.put("message", "活动不存在");
            return resultMap;
        }
        switch (fileType) {
            case 1:
                if (activity.getVolunteerTime() == null || !activity.getVolunteerTime().equals(uuid)) {
                    resultMap.put("status", "201");
                    resultMap.put("message", "该文件不存在");
                    return resultMap;
                }
                break;
            case 0:
                if (activity.getActivityProve() == null || !activity.getActivityProve().equals(uuid)) {
                    resultMap.put("status", "201");
                    resultMap.put("message", "该文件不存在");
                    return resultMap;
                }
                break;
                default:
                    resultMap.put("status","201");
                    resultMap.put("message","文件类型参数错误");
                    return resultMap;
        }
        ActivityProve queryActivityProve = new ActivityProve();
        queryActivityProve.setActivityId(activityID);
        // 数据库中，1是志愿时公示名单，0是活动证明
        List<ActivityProve> proveList = activityProveService.selectByActivityID(activityID);
        Iterator<ActivityProve> it = proveList.iterator();
        while(it.hasNext()){
            ActivityProve prove = it.next();
            if(prove.getType() != fileType){
                it.remove();
            }
        }
        resultMap.put("status","200");
        resultMap.put("parse_data",proveList);
        return resultMap;
    }

    private Map<String, Object> paramLost() {
        Map<String, Object> map = new HashMap<>();
        map.put("status", "002");
        map.put("message", "请求参数丢失");
        return map;
    }
}
