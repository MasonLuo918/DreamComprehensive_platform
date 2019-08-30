package com.Dream.controller;

import com.Dream.Enum.OauthAccountEnum;
import com.Dream.Enum.OauthTypeEnum;
import com.Dream.commons.result.Result;
import com.Dream.commons.result.ResultCodeEnum;
import com.Dream.commons.result.ResultMap;
import com.Dream.entity.Department;
import com.Dream.entity.Oauth;
import com.Dream.entity.type.UserType;
import com.Dream.service.DepartmentService;
import com.Dream.service.abstract_class.AbstractOauth;
import com.Dream.util.MD5Util;
import com.Dream.util.URLUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/oauth/yiban")
public class YiBanOauthController {

    @Autowired
    @Qualifier("yibanOauthService")
    private AbstractOauth oauthService;

    @Autowired
    private DepartmentService departmentService;

    private OauthAccountEnum accountEnum = OauthAccountEnum.YIBAN;

    private final String YI_BAN_AUTHORIZE_URL = "https://openapi.yiban.cn/oauth/authorize";

    private final String STATE = "123";

    /**
     * 获取易班登录的链接
     * {
     * "data": {
     * "login_url": "https://openapi.yiban.cn/oauth/authorize?STATE=123&redirect_uri=http://localhost:8090/oauth/yiban/redirect&client_id=7670ac12371bee65"
     * },
     * "status": "200",
     * "message": "请求成功"
     * }
     *
     * @return
     */
    @RequestMapping("/getLoginURL")
    public Result<Map> getLoginURL() {
        Map<String, String> params = new HashMap<>();
        params.put("client_id", accountEnum.getAppID());
        params.put("redirect_uri", accountEnum.getRedirectURL());
        params.put("STATE", STATE);
        String reallyURL = URLUtils.getURL(YI_BAN_AUTHORIZE_URL, params);
        Map<String, String> attr = new HashMap<>();
        attr.put("login_url", reallyURL);
        return new Result<Map>(attr);
    }

    /**
     * 用户用易班进行登录, 如果用户是首次登录，则需要
     * 进行初次登录的绑定操作
     *
     * @param session session会话
     * @param code    用户登录授权令牌
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public Result login(HttpSession session, @RequestParam("code") String code) {
        Map<String, Object> loginResult = oauthService.login(code, session);
        int status = (int) loginResult.get("status");
        ResultMap result = null;
        switch (status) {
            // 进行邮箱绑定
            case 0:
                result = new ResultMap(ResultCodeEnum.PERMIT_REDIRECT);
                result.put("open_id",loginResult.get("open_id"));
                result.put("open_type",loginResult.get("open_type"));
                break;
            // 成功
            case 1:
                result = new ResultMap(ResultCodeEnum.SUCCESS_LOGIN);
                result.put("open_id", loginResult.get("open_id"));
                result.put("open_type",loginResult.get("open_type"));
                result.put("user", session.getAttribute("user"));
                break;
            //  token失效
            case 2:
                result = new ResultMap(ResultCodeEnum.FAIL);
                break;
        }
        return result;
    }

    /**
     * @param account 要查询的account
     * @return 返回查询的结果
     */
    @RequestMapping(value = "/getAccount", method = RequestMethod.GET)
    public Result<Department> getAccount(@RequestParam("account") String account){
        Department department = departmentService.findByEmail(account);
        if(department == null){
            Result<Department> result = new Result<Department>(ResultCodeEnum.FAIL);
            result.setMessage("无该组织");
            return result;
        }
        return new Result<Department>(ResultCodeEnum.SUCCESS, department);
    }

    /**
     * 所需参数:(如果account已经存在的话)
     * {
     *     "open_id":"1234",
     *     "account":"masonluo918@gmail.com"
     * }
     * 所需参数:(如果account不存在的话)
     * {
     *     "open_id":"123",
     *     "account":"masonluo918@gmail.com",
     *     "password":"xxx",
     *     "dept_name":"学生党务",
     *     "college":"学院"
     * }
     * @param requestMap
     * @return
     */
    @RequestMapping("/bind")
    public Result bind(HttpSession session, @RequestBody Map<String, Object> requestMap){
        String account = (String) requestMap.get("account");
        String openID = (String) requestMap.get("open_id");
        if(account == null || openID == null){
            return new Result(ResultCodeEnum.PARAMS_LOST);
        }
        // 绑定在一起
        Oauth oauth = new Oauth();
        oauth.setOpenID(openID);
        oauth.setUserID(account);
        oauth.setOpenType(OauthTypeEnum.YIBAN);

        Department department = departmentService.findByEmail(account);
        // 如果是已有账户的情况
        if(department != null){
            if(oauthService.bind(openID, account)){
                // 保存入会话，进行登录
                session.setAttribute("user",department);
                session.setAttribute("userType", UserType.DEPARTMENT);
                // 返回之后，跳转回主页
                return new Result<Oauth>(ResultCodeEnum.SUCCESS, oauth);
            }else{
                return new Result(ResultCodeEnum.FAIL);
            }
        }
        // 如果没有账户，则新建一个账户
        String college = (String) requestMap.get("college");
        String dept_Name = (String) requestMap.get("dept_name");
        String password = (String) requestMap.get("password");
        // 加密
        password = MD5Util.getMD5(password);
        department = new Department();
        // 设置成已激活状态，暂不验证邮箱
        department.setStatus(1);
        department.setEmail(account);
        department.setCreateTime(LocalDate.now());
        department.setPassword(password);
        department.setDeptName(dept_Name);
        department.setCollege(college);
        int count = departmentService.insertDepartment(department);
        if(count == 0){
            return new Result(ResultCodeEnum.FAIL);
        }
        if(oauthService.bind(openID, account)){
            return new Result<Oauth>(ResultCodeEnum.SUCCESS, oauth);
        }
        return new Result(ResultCodeEnum.FAIL);
    }

    @RequestMapping("/redirect")
    public Result test() {
        ResultMap result = new ResultMap(ResultCodeEnum.SUCCESS);
        result.put("key", "value");
        return result;
    }
}