package com.Dream.controller;

import com.Dream.bean.SignedTokenProperty;
import com.Dream.bean.TokenProperty;
import com.Dream.commons.cache.Entity;
import com.Dream.entity.SignIn;
import com.Dream.service.SignedInService;
import com.Dream.util.ParamUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.params.Params;

import javax.faces.annotation.RequestMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/signed/")
public class SignInController {

    @Autowired
    private SignedInService singedInService;

    /**
     * 返回一个活动签到器的令牌，每次申请二维码需要将这个令牌发送至后台
     * @param tokenProperty
     * @return
     */
    @RequestMapping("/create")
    @ResponseBody
    public Map<String, Object> create(@RequestBody TokenProperty tokenProperty){
        Map<String, Object> resultMap = new HashMap<>();
        if(tokenProperty == null || tokenProperty.getId() == null || tokenProperty.getUrl() == null ||
                tokenProperty.getValidity() == null){
            resultMap.put("status","002");
            resultMap.put("message","传输参数丢失");
            return resultMap;
        }

        String token = singedInService.start(tokenProperty);
        resultMap.put("status","200");
        resultMap.put("token",token);
        resultMap.put("validity",tokenProperty.getValidity());
        return resultMap;
    }

    /**
     * 返回一个图片的base64串
     * @param requestMap
     * @return
     */
    @RequestMapping(value = "/getQRCode", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getQRCode(@RequestBody Map<String, Object> requestMap){
        String token = (String)requestMap.get("token");
        Map<String, Object> resultMap = new HashMap<>();
        if(token == null){
            resultMap.put("status","002");
            resultMap.put("message","传输参数丢失");
            return resultMap;
        }
        Entity<SignedTokenProperty> entity = singedInService.getQRCode(token);
        if(entity == null){
            resultMap.put("status","201");
            resultMap.put("message","token已过期,请重新生成token");
            return resultMap;
        }
        resultMap.put("status","200");
        resultMap.put("qr_code",entity.getValue().getImgBase64());
        System.out.println(resultMap.get("qr_code"));
        resultMap.put("validity",entity.getValue().getValidity());
        return resultMap;
    }

    /**
     * 需要二维码附带的token，该token用来判断验证码是否过期
     * @param requestMap
     * @return
     * {
     *     "status":"302",
     *     "activity_id":1,
     *     "activity_name":"IT先锋进社区",
     *     "token":"token"
     * }
     * 上面的token是一开始生成活动签到器的token，不是二维码附带的token,签到时传过来后台
     */
    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> redirect(@RequestBody Map<String, Object> requestMap){
        String token = (String) requestMap.get("token");
        Map<String, Object> resultMap = new HashMap<>();
        if(token == null){
            resultMap.put("status","002");
            resultMap.put("message","传输参数丢失");
            return resultMap;
        }
        Entity<SignedTokenProperty> entity = singedInService.redirect(token);
        if(entity == null){
            resultMap.put("status","303");
            resultMap.put("message","已过期，请重新扫描二维码");
            return resultMap;
        }
        resultMap.put("status","302");
        resultMap.put("activity_name",entity.getValue().getName());
        resultMap.put("token",entity.getValue().getToken());
        return resultMap;
    }

    @RequestMapping("/doSignedIn")
    @ResponseBody
    public Map<String, Object> doSignedIn(@RequestBody Map<String, Object> requestMap){
        Map<String, Object> resultMap = new HashMap<>();
        String token = (String) requestMap.get("token");
        String stuID = (String) requestMap.get("stuID");
        String stuName = (String) requestMap.get("stuName");
        String stuClass = (String) requestMap.get("stuProfessionAndClass");
        if(ParamUtil.hasNull(token, stuID, stuName, stuClass)){
            resultMap.put("status","201");
            resultMap.put("message","参数丢失");
            return resultMap;
        }
        SignIn temp = new SignIn();
        temp.setStuID(stuID);
        temp.setStuName(stuName);
        temp.setStuProfessionAndClass(stuClass);
        SignIn record = singedInService.doSignIn(token, temp);
        if(record == null){
            resultMap.put("status","201");
            resultMap.put("message","签到时间已过");
            return resultMap;
        }
        resultMap.put("status","200");
        resultMap.put("activity_id",record.getActivityID());
        resultMap.put("stu_name",record.getStuName());
        resultMap.put("stu_class", record.getStuProfessionAndClass());
        resultMap.put("stu_num", record.getStuID());
        return resultMap;
    }

    @RequestMapping("/getCheckInStatus")
    @ResponseBody
    public Map<String, Object> getCheckInStatus(@RequestBody Map<String, Object> requestMap){
        Map<String, Object> resultMap = new HashMap<>();
        Integer activityID = (Integer) requestMap.get("activity_id");
        if(activityID == null){
            resultMap.put("status","002");
            resultMap.put("message","请求参数丢失");
            return resultMap;
        }
        List<SignIn> results = singedInService.getSignInList(activityID);
        resultMap.put("status","200");
        resultMap.put("results",results);
        return resultMap;
    }
}
