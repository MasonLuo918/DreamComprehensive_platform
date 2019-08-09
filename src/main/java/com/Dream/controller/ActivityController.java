package com.Dream.controller;

import com.Dream.entity.Activity;
import com.Dream.entity.Department;
import com.Dream.entity.Section;
import com.Dream.entity.UploadFile;
import com.Dream.entity.type.FileType;
import com.Dream.service.ActivityProveParser;
import com.Dream.service.ActivityService;
import com.Dream.service.UploadFileListService;
import com.Dream.service.UploadFileService;
import com.Dream.util.ParamUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RequestMapping("/admin/activity")
@Controller
//@Scope("prototype")  //每次都创建一个示例
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private UploadFileListService uploadFileListService;

    @Autowired
    private ActivityProveParser parser;

    private final String TIME_PATTERN = "yyyy-MM-dd";
    /**
     * 上传活动证明材料，同时会新建一个活动证明
     * @param request http请求，获取当前会话
     * @param activityName 活动名称,必须参数
     * @param time 活动时间,必须
     * @param material 活动证明图片材料
     * @param volunDoc 志愿时文档
     * @param actDoc 活动分文档
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> uploadMaterial(HttpServletRequest request,
                                              @RequestParam("activity_name") String activityName,
                                              @RequestParam("time") String time,
                                              @RequestParam("material") MultipartFile material,
                                              @RequestParam(value = "volun_doc", required = false) MultipartFile volunDoc,
                                              @RequestParam(value = "act_doc", required = false) MultipartFile actDoc) throws IOException {
        // 返回jason数据
        Map<String, Object> resultMap = new HashMap<>();
        if(ParamUtil.hasNull(actDoc, time)){
            resultMap.put("status","002");
            resultMap.put("activityName", activityName);
            resultMap.put("time", time);
            resultMap.put("material", material);
            resultMap.put("volun_doc", volunDoc);
            resultMap.put("act_doc",actDoc);
            return resultMap;
        }
        Object user = request.getSession().getAttribute("user");

        // 创建一个活动，并插入数据库，获得活动id
        Activity activity = getNewActivity(time, activityName, user);
        activityService.insert(activity);

        // 获取上下文路径以及构造文件上传的路径
        String contextPath = request.getSession().getServletContext().getRealPath("upload");
        String parentPath;
        UploadFile file = null;
        if (material != null) {
            parentPath = getParentPath(contextPath, request, time, FileType.MATERIAL);
            file = uploadFileService.uploadSimpleFile(parentPath, material, activity.getId());
            if (file != null) {
                activity.setMaterial(file.getUuid());
            }
        }
        if (volunDoc != null) {
            parentPath = getParentPath(contextPath, request, time, FileType.VOLUNTEER_DOC);
            file = uploadFileService.uploadSimpleFile(parentPath, volunDoc, activity.getId());
            if (file != null) {
                activity.setVolunteerTime(file.getUuid());
            }
        }
        if (actDoc != null) {
            parentPath = getParentPath(contextPath, request, time, FileType.ACTIVITY_PROVE_DOC);
            file = uploadFileService.uploadSimpleFile(parentPath, actDoc, activity.getId());
            if (file != null) {
                activity.setActivityProve(file.getUuid());
            }
        }
        // 更新一遍活动
        activityService.update(activity);
        resultMap.put("status", "200");
        resultMap.put("activity", activity);
        return resultMap;
    }

    /**
     * 获取一个部门或者一个组织的所有活动
     * 使用Restful api 请求格式，向一个url发送请求：
     * /admin/activity/getAllActivity/1/10
     * 其中，上面的1代表第几页，size代表每一页的条数
     * @param page 第几页
     * @param size 每一页的条数
     * @return
     * {
     *     "status":"200",
     *     "activity":[
     *          {
     *              "id":"",
     *              "name":"",
     *              "time":"",
     *              "material":"",
     *              "volunteer_time":"",
     *              "activity_prove":"",
     *              "department_id":"",
     *              "section_id":""
     *          },...
     *     ]
     * }
     *
     * {
     *     "status":"002",
     *     "message":"传送参数丢失"
     * }
     *
     * {
     *     "status":"403",
     *     "message:"用户未登录"
     * }
     */
    @RequestMapping("getAllActivity/{page}/{size}")
    @ResponseBody
    public Map<String, Object> getAllActivity(HttpSession session, @PathVariable("page") int page, @PathVariable(value = "size") int size){
        Map<String, Object> resultMap = new HashMap<>();
        Integer departmentID = null;
        Integer sectionID = null;
        Object user = session.getAttribute("user");
        if(user instanceof Department){
            Department department = (Department) user;
            departmentID = department.getId();
        }else if(user instanceof Section){
            Section section = (Section) user;
            departmentID = section.getDepartmentID();
            sectionID = section.getId();
        }
        int activityCount = activityService.activityCount(departmentID, sectionID);
        List<Activity> list = activityService.findByRegisterID(departmentID, sectionID, page, size);
        resultMap.put("status","200");
        resultMap.put("activities",list);
        resultMap.put("page",page);
        resultMap.put("size",size);
        boolean hasNextPage = false;
        if(activityCount > (page - 1) * size + list.size()){
            hasNextPage = true;
        }else{
            hasNextPage = false;
        }
        resultMap.put("has_next_page",hasNextPage);
        return resultMap;
    }


    public boolean sameUser(HttpSession session, Integer departmentID, Integer sectionID){
        Object user = session.getAttribute("user");
        if(user != null){
            if(user instanceof Department){
                Department department = (Department) user;
                if(departmentID == null || !department.getId().equals(departmentID)){
                    return false;
                }
            }
            if(user instanceof Section){
                Section section = (Section) user;
                if(departmentID == null || sectionID == null || !section.getDepartmentID().equals(departmentID) ||
                        !section.getId().equals(sectionID)){
                    return false;
                }
            }
            return true;
        }
        return false;
    }


    /**
     * 获取上传文件的父目录路径
     *
     * @param request
     * @param time
     * @param type
     * @param realPath 项目真实路径
     * @return
     */
    public String getParentPath(String realPath, HttpServletRequest request, String time, Integer type) {
        StringBuilder builder = new StringBuilder();
        //项目的真实路径
        builder.append(realPath);
        builder.append(File.separator);
        HttpSession session = request.getSession();
        // 获取登录的user
        Object user = session.getAttribute("user");
        // 判断user类型，根据user类型构建组织文件夹
        if (user instanceof Department) {
            Department department = (Department) user;
            builder.append(department.getCollege());
            builder.append(File.separator);
            builder.append(department.getDeptName());
            builder.append(File.separator);
        } else if (user instanceof Section) {
            Section section = (Section) user;
            builder.append(section.getDepartment().getCollege());
            builder.append(File.separator);
            builder.append(section.getDepartment().getDeptName());
            builder.append(File.separator);
            builder.append(section.getName());
            builder.append(File.separator);
        }
        time = time.replace("-", File.separator);
        builder.append(time);
        builder.append(File.separator);
        switch (type) {
            case FileType.MATERIAL:
                builder.append("material");
                break;
            case FileType.ACTIVITY_PROVE_DOC:
                builder.append("activity_prove_doc");
                break;
            case FileType.VOLUNTEER_DOC:
                builder.append("volunteer_doc");
                break;
            default:
                break;
        }
        return builder.toString();
    }

    public String getParentPath(HttpServletRequest request, String time, Integer type) {
        return getParentPath("", request, time, type);
    }

    /**
     * @param time          时间，格式为"2018-09-22"
     * @param activityName  "活动名称"
     * @param user          用户
     * @param material      材料uuid
     * @param volunteerTime 志愿时uuid
     * @param activityProve 活动证明uuid
     * @return 一个activity
     */
    public Activity getNewActivity(String time, String activityName, Object user, String material, String volunteerTime,
                                   String activityProve) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_PATTERN);
        LocalDate date = LocalDate.parse(time, formatter);
        Activity activity = new Activity();
        activity.setName(activityName);
        activity.setTime(date);
        if (user != null) {
            if (user instanceof Department) {
                Department department = (Department) user;
                activity.setDepartmentID(department.getId());
            } else if (user instanceof Section) {
                Section section = (Section) user;
                activity.setDepartmentID(section.getDepartmentID());
                activity.setSectionID(section.getId());
            }
        }
        activity.setMaterial(material);
        activity.setVolunteerTime(volunteerTime);
        activity.setActivityProve(activityProve);
        return activity;
    }

    public Activity getNewActivity(String time, String activityName, Object user) {
        return getNewActivity(time, activityName, user, null, null, null);
    }

    public Map<String, Object> paramLost(){
        Map<String, Object> map = new HashMap<>();
        map.put("status","002");
        map.put("message","请求参数丢失");
        return map;
    }
}
