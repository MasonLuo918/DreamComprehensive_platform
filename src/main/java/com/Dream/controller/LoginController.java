package com.Dream.controller;

import com.Dream.entity.Department;
import com.Dream.entity.Section;
import com.Dream.entity.type.UserType;
import com.Dream.service.DepartmentService;
import com.Dream.service.SectionService;
import com.Dream.util.ParamUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class LoginController {
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private SectionService sectionService;

    /**
     * 前端传送的数据例子:
     * {
     * "userName":"email or account",
     * "password":"password",
     * "type":1,（详见api文档）
     * "code":"abcd"
     * }
     * <p>
     * 后端根据登录类型，返回数据:
     * {
     * "status":"500",
     * "user":{
     * (不同的类型用户有不同的信息)
     * }
     * "userType":1(同type)
     * }
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/login")
    @ResponseBody
    public Map<String, Object> login(HttpServletRequest request, @RequestBody Map<String, Object> map, @SessionAttribute(value = "validateCode", required = false) String validateCode) {
        // 获取所有信息
        Map<String, Object> resultMap = new HashMap<>();
        HttpSession session = request.getSession();
        String userName = (String) map.get("userName");
        String password = (String) map.get("password");
        String code = (String) map.get("code");
        int type = (int) map.get("type");
        //判断信息是否都为空
        if (ParamUtil.hasNull(userName, password, code, type)) {
            resultMap.put("status", "002");
            resultMap.put("params", map);
            //去除密码
            map.remove("password");
            return resultMap;
        }
        //确认验证码信息
//        if(!code.equals(validateCode)){
//            resultMap.put("status", "001");
//            return resultMap;
//        }
        // 判断是否已经登录
        Object user = session.getAttribute("user");
        if (user != null) {
            resultMap.put("status", "103");
            resultMap.put("user", user);
            resultMap.put("userType", ParamUtil.getUserType(user));
            return resultMap;
        }
        // 获取登录信息，如果登录成功，则将信息写入session中,session 中写入两个信息，user和userType
        Map<String, Object> queryMap = null;
        switch (type) {
            case UserType.DEPARTMENT:
                queryMap = doDepartmentLogin(userName, password);
                break;
            case UserType.SECTION:
                queryMap = doSectionLogin(userName, password);
                break;
                default:break;
        }
        /*
         根据登录返回的信息进行判断
         */
        int status = (int)queryMap.get("status");
        switch (status){
            case 0:
                resultMap.put("status", "401");
                resultMap.put("userName",userName);
                break;
            case 1:
                resultMap.put("status", "101");
                // 将用户添加到session中
                session.setAttribute("user", queryMap.get("user"));
                session.setAttribute("userType",queryMap.get("userType"));
                resultMap.put("user", session.getAttribute("user"));
                resultMap.put("userType", queryMap.get("userType"));
                break;
            case 2:
                resultMap.put("status", "404");
                resultMap.put("userName",userName);
                break;
        }
        return resultMap;
    }

    /**
     * map 中含有两个信息，一个是登录状态码，还有一个就是查询出来的用户信息
     * "status" 0 代表账号或者密码错误, 1 代表登录成功, 2 代表未激活
     * "user" 为查询出来的信息
     * @param email
     * @param password
     * @return
     */
    private Map<String, Object> doDepartmentLogin(String email, String password){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("userType", UserType.DEPARTMENT);
        Department result = departmentService.login(email, password);
        if(result != null && result.getStatus() == 1){
            resultMap.put("status", 1);
            resultMap.put("user", result);
            return resultMap;
        }else if(result == null){
            resultMap.put("status", 0);
            return resultMap;
        }else{
            resultMap.put("status", 2);
            return resultMap;
        }
    }

    private Map<String, Object> doSectionLogin(String account, String password){
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("userType",UserType.SECTION);
        Section result=sectionService.login(account,password);
        if(result!=null&&result.getStatus()==1){
            resultMap.put("status",1);
            resultMap.put("user",result);
            return resultMap;
        }else if(result==null){
            resultMap.put("status",0);
            return resultMap;
        }else{
            resultMap.put("status",2);
            return resultMap;
        }
    }


    @RequestMapping("/test")
    @ResponseBody
    public Map test(){
        Map map = new HashMap();
        map.put("test","success");
        return map;
    }
}
