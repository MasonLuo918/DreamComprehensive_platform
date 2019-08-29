package com.Dream.controller;

import com.Dream.commons.bean.SignedTokenProperty;
import com.Dream.commons.bean.TokenProperty;
import com.Dream.commons.cache.Entity;
import com.Dream.entity.Activity;
import com.Dream.entity.SignIn;
import com.Dream.service.ActivityService;
import com.Dream.service.SignedInService;
import com.Dream.util.ExportWordUtil;
import com.Dream.util.ParamUtil;
import org.apache.ibatis.type.JdbcType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/signed/")
public class SignInController {

    // TODO 如果已经签到，则不允许签到了

    @Autowired
    private SignedInService singedInService;

    @Autowired
    private ActivityService activityService;

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
        System.out.println(token);
        resultMap.put("validity",tokenProperty.getValidity());
        return resultMap;
    }

    /**
     * 返回一个图片的base64串
     * @param token
     * @return
     */
    @RequestMapping(value = "/getQRCode", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getQRCode(@RequestParam("token") String token){
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
        resultMap.put("validity",entity.getValue().getValidity());
        return resultMap;
    }

    /**
     * 需要二维码附带的token，该token用来判断验证码是否过期
     * @param token
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
    public Map<String, Object> redirect(@RequestParam("token") String token){
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

    @RequestMapping("/export")
    public void exportWord(HttpServletResponse response, @RequestParam("activity_id") Integer activityID) throws IOException {
//        Integer activityID = (Integer) requestMap.get("activity_id");
        List<SignIn> list = singedInService.getSignInList(activityID);
        Activity activity = activityService.findByID(activityID);
        XWPFDocument doc = ExportWordUtil.export(list);
        OutputStream outputStream = response.getOutputStream();
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName="+URLEncoder.encode(activity.getName() + ".docx", "UTF-8"));
        doc.write(outputStream);
    }
}
