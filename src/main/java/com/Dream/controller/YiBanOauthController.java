package com.Dream.controller;

import com.Dream.Enum.OauthAccountEnum;
import com.Dream.commons.result.Result;
import com.Dream.commons.result.ResultCodeEnum;
import com.Dream.commons.result.ResultMap;
import com.Dream.entity.Department;
import com.Dream.service.DepartmentService;
import com.Dream.service.abstract_class.AbstractOauth;
import com.Dream.util.URLUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
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
    @ResponseBody
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
    @ResponseBody
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
    @RequestMapping("/getAccount")
    @ResponseBody
    public Result<Department> getAccount(@RequestParam("account") String account){
        Department department = departmentService.findByEmail(account);
        if(department == null){
            Result<Department> result = new Result<Department>(ResultCodeEnum.FAIL);
            result.setMessage("无该组织");
            return result;
        }
        return new Result<Department>(ResultCodeEnum.SUCCESS, department);
    }

    @RequestMapping("/redirect")
    @ResponseBody
    public Result test() {
        ResultMap result = new ResultMap(ResultCodeEnum.SUCCESS);
        result.put("key", "value");
        return result;
    }
}