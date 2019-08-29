package com.Dream.service.impl;

import com.Dream.Enum.OauthAccountEnum;
import com.Dream.Enum.OauthTypeEnum;
import com.Dream.commons.result.ResultMap;
import com.Dream.dao.DepartmentDao;
import com.Dream.dao.OauthDao;
import com.Dream.entity.Department;
import com.Dream.entity.Oauth;
import com.Dream.entity.type.UserType;
import com.Dream.service.abstract_class.AbstractOauth;
import com.Dream.util.URLUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service("yibanOauthService")
@Scope("prototype")
public class YiBanOauthServiceImpl extends AbstractOauth {

    @Autowired
    private DepartmentDao departmentDao;

    /**
     * 易班的获取Access_Token登录的
     * 返回数据详看https://o.yiban.cn/wiki/index.php?page=oauth/access_token
     * 成功返回:
     * {
     *   "access_token":"授权凭证",
     *   "userid":"授权用户id",
     *   "expires":"截止有效期"
     * }
     *
     * 失败返回:
     * {
     *   "status":"error",
     *   "info":{
     *     "code":"错误编号",
     *     "msgCN":"中文报错信息",
     *     "msgEN":"英文报错信息"
     *   }
     * }
     * @param code 令牌
     * @return
     */
    @Override
    public Map<String, String> getAccessToken(String code) {
        Map<String, String > params = new HashMap<>();
        params.put("client_id",OauthAccountEnum.YIBAN.getAppID());
        params.put("client_secret",OauthAccountEnum.YIBAN.getAppSecret());
        params.put("code",code);
        params.put("redirect_uri",OauthAccountEnum.YIBAN.getRedirectURL());
        try {
            Map<String, String> result = URLUtils.doRequest("https://openapi.yiban.cn/oauth/access_token",params,"POST");
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 详见https://o.yiban.cn/wiki/index.php?page=user/me
     * 成功返回：
     * {
     *   "status":"success",
     *   "info":{
     *     "yb_userid":"易班用户id",
     *     "yb_username":"用户名",
     *     "yb_usernick":"用户昵称",
     *     "yb_sex":"性别",
     *     "yb_money":"持有网薪",
     *     "yb_exp":"经验值",
     *     "yb_userhead":"用户头像",
     *     "yb_schoolid":"所在学校id",
     *     "yb_schoolname":"所在学校名称"
     *   }
     * }
     *
     * 失败返回：
     * {
     *   "status":"error",
     *   "info":{
     *     "code":"错误编号",
     *     "msgCN":"中文报错信息",
     *     "msgEN":"英文报错信息"
     *   }
     * }
     * @param accessToken
     * @return
     */
    @Override
    public Map getOpenID(String accessToken) {
        Map<String, String> param = new HashMap<>();
        param.put("access_token",accessToken);
        try {
            Map<String, String> resultMap = URLUtils.doRequest("https://openapi.yiban.cn/user/me",param,"GET");
            return resultMap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Map<String, Object> login(String code, HttpSession session) {
        Map<String, String> queryResult = getAccessToken(code);
        Map<String, Object> responseMap = new HashMap<>();
        // 如果成功是没有status的
        if(queryResult == null || (queryResult.get("status") != null && queryResult.get("status").equals("error"))){
            responseMap.put("status", 2);
            responseMap.put("message","无效code或code已经失效");
            return responseMap;
        }
        String accessToken = queryResult.get("access_token");
        String openID = queryResult.get("userid");
        Oauth oauth = new Oauth();
        oauth.setOpenID(openID);
        // 判断是否已经绑定了
        oauth = oauthDao.selectOne(oauth);
        if(oauth == null){
            responseMap.put("status", 0);
            responseMap.put("message","用户首次登录，请进行账户绑定");
            responseMap.put("open_id",openID);
            responseMap.put("open_type", OauthTypeEnum.YIBAN.getId());
            return responseMap;
        }
        String account = oauth.getUserID();
        // 这里还需要判断是部门组织或者是啥，这里不作判断
        oauth.setAccessToken(accessToken);
        oauth.setExpiredTime((Long.valueOf(queryResult.get("expires"))));
        oauthDao.update(oauth);
        Department department = departmentDao.selectByEmail(account);
        session.setAttribute("user", department);
        session.setAttribute("userType",UserType.DEPARTMENT);
        responseMap.put("status", 1);
        responseMap.put("message", "登录成功");
        responseMap.put("open_id", openID);
        responseMap.put("open_type", OauthTypeEnum.YIBAN.getId());
        return responseMap;
    }

    @Override
    public boolean bind(String openID, String account) {
        Oauth oauth = new Oauth();
        oauth.setOpenType(OauthTypeEnum.YIBAN);
        Department department = departmentDao.selectByEmail(account);
        if(department == null){
            return false;
        }
        oauth.setUserID(account);
        oauth.setOpenID(openID);
        int count = oauthDao.insert(oauth);
        if(count != 0){
            return true;
        }
        return false;
    }
}
