package com.Dream.controller;

import com.Dream.commons.bean.ActivityResult;
import com.Dream.entity.*;
import com.Dream.entity.type.FileType;
import com.Dream.service.*;
import com.Dream.util.ParamUtil;
import org.apache.ibatis.annotations.Param;
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
    private ActivityProveService activityProveService;

    @Autowired
    private ActivityProveParser parser;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
                                              @RequestParam(value = "material", required = false) MultipartFile material,
                                              @RequestParam(value = "volun_doc", required = false) MultipartFile volunDoc,
                                              @RequestParam(value = "act_doc", required = false) MultipartFile actDoc) throws IOException {
        // 返回json数据
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
    @RequestMapping("getAllActivity")
    @ResponseBody
    public Map<String, Object> getAllActivity(HttpSession session){
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
        List<Activity> list = activityService.findByRegisterID(departmentID, sectionID);
        List<ActivityResult> returnList = new ArrayList<>();
        for(Activity activity:list){
            ActivityResult result = new ActivityResult();
            result.setId(activity.getId());
            result.setName(activity.getName());
            result.setMaterial(activity.getMaterial());
            result.setVolunteer_time(activity.getVolunteerTime());
            result.setActivity_prove(activity.getActivityProve());
            result.setTime(activity.getTime().format(formatter));
            returnList.add(result);
        }
        resultMap.put("status","200");
        resultMap.put("activities",returnList);
        return resultMap;
    }

    /**
     * 编辑一个活动，发送一个表单数据过来
     * @param request http请求
     * @param activityID 活动id 必须
     * @param activityName 活动名称 必须
     * @param time 活动时间 必须
     * @param material 活动材料 非必须
     * @param volunDoc 志愿证明 非必须
     * @param actDoc   活动证明 非必须
     * @return
     * @throws IOException
     * 返回示例：
     * {
     *     "updateActivity": {
     *         "id": 102,
     *         "name": "IT先锋进社区",
     *         "time": {
     *             "year": 2017,
     *             "month": "FEBRUARY",
     *             "monthValue": 2,
     *             "dayOfMonth": 2,
     *             "dayOfWeek": "THURSDAY",
     *             "era": "CE",
     *             "dayOfYear": 33,
     *             "leapYear": false,
     *             "chronology": {
     *                 "id": "ISO",
     *                 "calendarType": "iso8601"
     *             }
     *         },
     *         "departmentID": null,
     *         "sectionID": null,
     *         "material": null,
     *         "volunteerTime": "80a97e8542044d44b1f35dc8ed6dc6cf",
     *         "activityProve": "8dcb1ec46715483d928848b89fd96633",
     *         "materialFile": null,
     *         "volunteerTimeFile": null,
     *         "activityProveFile": null,
     *         "department": null
     *     },
     *     "status": "200"
     * }
     */
    @RequestMapping(value = "/editActivity", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> editActivity(HttpServletRequest request, @RequestParam("activity_id") Integer activityID,
                                            @RequestParam("activity_name") String activityName,
                                            @RequestParam("time") String time,
                                            @RequestParam(value = "material", required = false) MultipartFile material,
                                            @RequestParam(value = "volun_doc", required = false) MultipartFile volunDoc,
                                            @RequestParam(value = "act_doc", required = false) MultipartFile actDoc) throws IOException {
        if(ParamUtil.hasNull(activityID, activityName, time)){
            return paramLost();
        }
        String realPath = request.getSession().getServletContext().getRealPath("upload");
        String parentPath = null;
        Map<String, Object> resultMap = new HashMap<>();
        Activity updateActivity = new Activity();
        // 新建一个用来更新的activity
        updateActivity.setId(activityID);
        updateActivity.setName(activityName);
        updateActivity.setTime(LocalDate.parse(time,formatter));
        UploadFile file = null;
        // 如果不为null，则重新上传活动证明材料，然后更新activity中的material字段
        if(material != null){
            parentPath = getParentPath(realPath, request, time, FileType.MATERIAL);
            file = uploadFileService.uploadSimpleFile(parentPath, material, activityID);
            updateActivity.setMaterial(file.getUuid());
        }
        // 如果文档不为null，则将之前的记录本全部删除，然后重新上传文档，并进行解析
        if(volunDoc != null){
            // 清除数据库中解析出来的记录
            activityProveService.clearDocRecord(activityID, 1);
            parentPath = getParentPath(realPath, request, time, FileType.VOLUNTEER_DOC);
            file = uploadFileService.uploadSimpleFile(parentPath, volunDoc, activityID);
            updateActivity.setVolunteerTime(file.getUuid());
        }
        if(actDoc != null){
            // 清除数据库中解析出来的记录
            activityProveService.clearDocRecord(activityID, 0);
            parentPath = getParentPath(realPath, request, time, FileType.ACTIVITY_PROVE_DOC);
            file = uploadFileService.uploadSimpleFile(parentPath, actDoc, activityID);
            updateActivity.setActivityProve(file.getUuid());
        }
        // 更新活动数据
        activityService.update(updateActivity);
        resultMap.put("status", "200");
        resultMap.put("updateActivity", updateActivity);
        return resultMap;
    }

    /**
     * 删除一个活动
     * 选择一个活动进行删除
     * 参数:
     * activity_id: 该活动的id
     * @param map
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteActivity(@RequestBody Map<String, Object> map){
        Map<String, Object> resultMap = new HashMap<>();
        Integer activityID = (Integer) map.get("activity_id");
        if(activityID == null){
            return paramLost();
        }
        activityService.deleteActivity(activityID);
        resultMap.put("status","200");
        resultMap.put("message","成功删除活动");
        resultMap.put("activity_id", activityID);
        return resultMap;
    }

    /**
     * 判断是否是同一个用户
     * @param session
     * @param departmentID
     * @param sectionID
     * @return
     */
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
