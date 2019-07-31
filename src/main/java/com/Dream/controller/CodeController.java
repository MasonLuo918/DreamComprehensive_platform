package com.Dream.controller;

import com.Dream.util.ValidateCode;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CodeController {

    /**
     * 根据会话中的验证码和前端传来的验证码进行判断，判断验证码是否正确
     * 返回一个json字符串，格式如下:
     * {
     *     "status":0,
     *     "message":"验证通过"
     * }
     *
     * {
     *     "status":1,
     *     "message":"验证失败,请重新输入验证码"
     * }
     * @param vCode
     * @param code
     * @return
     */
    @RequestMapping("/checkCode")
    @ResponseBody
    public Map<String, Object> checkCode(@SessionAttribute(value = "validateCode", required = false) String vCode, @RequestParam(value = "code") String code){
        Map<String, Object> resultMap = new HashMap<>();
        if(vCode == null || code == null || StringUtils.isEmpty(code) || StringUtils.isEmpty(vCode)){
            resultMap.put("status", 1);
            resultMap.put("message","验证失败，请重新输入验证码");
            return resultMap;
        }
        if(!code.equalsIgnoreCase(vCode)){
            resultMap.put("status", 1);
            resultMap.put("message","验证失败，请重新输入验证码");
            return resultMap;
        }else{
            resultMap.put("status", 0);
            resultMap.put("message","验证通过");
            return resultMap;
        }
    }

    @RequestMapping("/validateCode")
    public void getCode(HttpServletRequest httpRequest, HttpServletResponse response){
        try {
            String code = ValidateCode.generateCode(100, 40, "jpeg", response.getOutputStream());
            httpRequest.getSession().setAttribute("validateCode",code);
            System.out.println(code);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
