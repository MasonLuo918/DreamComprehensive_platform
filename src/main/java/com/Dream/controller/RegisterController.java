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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
     * "info":"注册成功，请前往邮箱激活",
     * "department":{
     * "id",1,
     * "email":"xxx@email.com",
     * "deptName":"deptName",
     * "college":"college"
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
    @RequestMapping("/doRegister")
    @ResponseBody
    public Map<String, Object> register(@RequestBody Map<String, String> map,
                                        @SessionAttribute(value = "validateCode") String validateCode) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        //预先失败信息
        resultMap.put("status", 1);
        resultMap.put("message", "注册失败,请检查注册信息");
        String code = map.get("code");
        /*
         * 检查验证码信息,线下测试时注释掉代码,方便测试数据
         */
        if (code == null || validateCode == null || StringUtils.isEmpty(code) || StringUtils.isEmpty(validateCode) ||
                !code.equalsIgnoreCase(validateCode)) {
            resultMap.put("info", "验证码错误，请重新输入");
            return resultMap;
        }
        String password = MD5Util.getMD5(map.get("password"));
        String email = map.get("email");
        String college = map.get("college");
        String deptName = map.get("deptName");
        if (password == null || email == null || college == null || deptName == null) {
            resultMap.put("message", "注册失败,请检查注册信息");
            resultMap.put("info", "所需参数有丢失");
            return resultMap;
        }
        Department department = new Department();
        department.setPassword(password);
        department.setEmail(email);
        department.setCollege(college);
        department.setDeptName(deptName);
        department.setStatus(0);
        boolean allowRegister = departmentService.allowRegister(department);
        if (!allowRegister) {
            return resultMap;
        } else {
            int insertRow = departmentService.insertDepartment(department);
            try {
                if (insertRow != 0) {
                    department.setPassword(null);
                    resultMap.clear();
                    resultMap.put("status", 0);
                    resultMap.put("message", "success");
                    resultMap.put("info", "注册成功，请前往邮箱激活");
                    resultMap.put("department", department);
                    String activationCode = MD5Util.getMD5(email + System.currentTimeMillis() + map.get("password"));
                    redisTemplate.opsForValue().set(department.getEmail(), activationCode, 24, TimeUnit.HOURS);
                    SendEmail.sendMail(department.getEmail(), activationCode, department.getDeptName());
                    return resultMap;
                } else {
                    resultMap.put("info", "该组织已组册");
                    return resultMap;
                }
            } catch (Exception e) {
                resultMap.put("message","fail");
                resultMap.put("status",1);
                resultMap.remove("department");
                resultMap.put("info", "发送邮件错误,注册失败");
                departmentService.deleteDepartment(department.getEmail());
                e.printStackTrace();
                return resultMap;
            }
        }
    }
}
