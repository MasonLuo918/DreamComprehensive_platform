package com.Dream.controller;

import com.Dream.entity.Department;
import com.Dream.mail.SendEmail;
import com.Dream.service.DepartmentService;
import com.Dream.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 前端注册的时候，传来一个department的信息，使用json字符串，如下:
     * {
     * "email":"1066812978@eamil.com",
     * "password":"password",
     * "deptName":"学生党务管理委员会",
     * "college":"数学与信息学院",
     * "code":"djfkasd"
     * }
     * <p>
     * 然后后台进行验证，是否可以注册，然后返回一个jason字符串,如下：
     * {
     * "status":0,
     * "message":"success",
     * "info":"注册成?功，请前往邮箱激活",
     * "department":{
     * "id",1,
     * "email":"xxx@email.com",
     * "deptName":"deptName",
     * "college":"college",
     * "createTime":"",
     * }
     * }
     * <p>
     * {
     * "status":1,
     * "message":"注册失败,请检查注册信息",
     * "info":"所需参数有丢失" （"该组织已注册"）("验证码错误")
     * }
     *
     * @param map
     * @param validateCode
     * @return
     */
    @RequestMapping("/department/doRegister")
    @ResponseBody
    public Map<String, Object> register(@RequestBody Map<String, String> map,
                                        @SessionAttribute(value = "validateCode") String validateCode) throws Exception {

        Map<String, Object> responseMap = new HashMap<>();

        //获取前端传来的校验信息
        String registerEmail = map.get("email");
        String registerPassword = map.get("password");
        String registerDeptName = map.get("deptName");
        String registerCode = map.get("code");
        String registerCollege = map.get("college");
        //判断验证码是否正确,以及所需要参数是否已经传来
        if (registerEmail == null || registerPassword == null || registerCode == null || registerDeptName == null
                || registerCollege == null) {
            responseMap.put("status", "002");
            responseMap.put("message", "注册失败,请检查注册信息");
            responseMap.put("info", "所需参数有丢失");
            return responseMap;
        }
        if (StringUtils.isEmpty(validateCode) || StringUtils.isEmpty(registerCode) || !registerCode.equals(validateCode)) {
            responseMap.put("status", "001");
            responseMap.put("message", "验证码错误或者已失效");
            return responseMap;
        }
        // 判断该email是否已经注册，如果已经注册，则重新发送验证信息
        // 否则，则新建一条记录
        Department queryDepartment = departmentService.findByEmail(registerEmail);
        Department resultDepartment = null;
        if (queryDepartment != null) {
            if (queryDepartment.getStatus() == 0) {
                String oldCode = (String) redisTemplate.opsForValue().get(registerEmail);
                if (oldCode != null) {
                    responseMap.put("status", "003");
                    responseMap.put("message", "fail");
                    responseMap.put("info", " 激活码未过期");
                    return responseMap;
                }else{
                    //重新生成激活码
                    String activateCode = MD5Util.getMD5(registerEmail + registerPassword + System.currentTimeMillis());
                    redisTemplate.opsForValue().set(registerEmail, activateCode);
                    //发送邮件
                    SendEmail.sendMail(registerEmail, activateCode, registerDeptName);
                    responseMap.put("status", "004");
                    responseMap.put("message", "success");
                    responseMap.put("info", "注册成功，请前往邮箱激活");
                    //将密码信息清空
                    queryDepartment.setPassword(null);
                    responseMap.put("department", queryDepartment);
                    return responseMap;
                }
            } else {
                responseMap.put("status", "003");
                responseMap.put("message", "fail");
                responseMap.put("info", " 邮箱已注册");
                return responseMap;
            }
        }
        // 判断是否该学院的该组织已经注册过，如果已经注册过，则不允许注册
        queryDepartment = departmentService.findByCollegeAndDeptName(registerCollege, registerDeptName);
        if (queryDepartment != null) {
            responseMap.put("status", "003");
            responseMap.put("message", "fail");
            responseMap.put("info", " 组织已注册");
            return responseMap;
        } else {
            resultDepartment = new Department();
            resultDepartment.setDeptName(registerDeptName);
            resultDepartment.setCollege(registerCollege);
            resultDepartment.setPassword(MD5Util.getMD5(registerPassword));
            resultDepartment.setCreateTime(LocalDate.now());
            resultDepartment.setEmail(registerEmail);
            resultDepartment.setStatus(0);
            //生成激活验证码
            String activateCode = MD5Util.getMD5(registerEmail + registerPassword + System.currentTimeMillis());
            int i = departmentService.register(resultDepartment,activateCode);
            if (i != 0) {
                responseMap.put("status", "004");
                responseMap.put("message", "success");
                responseMap.put("info", "注册成功，请前往邮箱激活");
                //将密码信息清空
                resultDepartment.setPassword(null);
                responseMap.put("department", resultDepartment);
                return responseMap;
            } else {
                responseMap.put("status", "000");
                responseMap.put("message", "fail");
                responseMap.put("info", "注册失败，请重新注册");
                return responseMap;
            }
        }
    }

    /**
     * 用户点击激活链接,进入到这个控制器
     * 点击时有如下几种情况：
     * 1、激活成功，将数据库status设为0
     * {
     * "status":0,
     * "message":"success"
     * }
     * <p>
     * }
     * 2、激活失败，激活码不符或者已经超时
     * {
     * "status":1,
     * "message":"激活码错误或者已经超时，请重新注册" ("该邮箱尚未注册")
     * }
     *
     * @param email        个人邮箱
     * @param validateCode 链接中的注册码
     * @return
     */
    @RequestMapping("/department/activate")
    @ResponseBody
    public Map<String, Object> activate(@RequestParam("email") String email, @RequestParam("validateCode") String validateCode) {
        Map<String, Object> resultMap = new HashMap<>();
        Department department = departmentService.findByEmail(email);
        if (department == null) {
            resultMap.put("status", 1);
            resultMap.put("message", "该邮箱尚未注册");
            return resultMap;
        }

        String redisCode = (String) redisTemplate.opsForValue().get(email);
        if (redisCode == null || !redisCode.equals(validateCode)) {
            resultMap.put("status", 1);
            resultMap.put("message", "激活码错误或者已经超时，请重新注册");
            return resultMap;
        }
        Department updateDepartment = new Department();
        updateDepartment.setId(department.getId());
        updateDepartment.setStatus(1);
        departmentService.update(updateDepartment);
        resultMap.put("status", 0);
        resultMap.put("message", "激活成功");
        return resultMap;
    }
    /**
     * 判断邮箱是否输入正确
     */
    @ResponseBody
    public Map<String,Object> checkEmail(@RequestParam("email") String email){
        Map<String,Object> resultMap=new HashMap<>();
        String string ="^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p;
        Matcher m;
        p=Pattern.compile(string);
        m=p.matcher(email);
        if(m.matches()){
            resultMap.put("status","005");
            resultMap.put("message","输入的邮箱格式正确");
            return resultMap;
        }else{
            resultMap.put("status","401");
            resultMap.put("message","输入的邮箱格式错误");
            return resultMap;
        }
    }
    /**
     * 查看组织是否存在
     */
    @ResponseBody
    public Map<String,Object> checkSection(@RequestParam("deptName") String deptName,@RequestParam("college") String college){
       Map<String,Object> resultMap=new HashMap<>();
       Department department=departmentService.findByCollegeAndDeptName(college,deptName);
       if(department==null){
          resultMap.put("status","005");
          resultMap.put("message","组织不存在，可以注册。");
          return resultMap;
       }else{
           resultMap.put("status","003");
           resultMap.put("message","该学院该组织已存在");
       }
       return resultMap;
    }

}
