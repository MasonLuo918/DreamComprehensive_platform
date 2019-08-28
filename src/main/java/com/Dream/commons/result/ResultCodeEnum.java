package com.Dream.commons.result;

public enum ResultCodeEnum {
    UNKNOW("000", "未知错误"),
    VALIDATE_CODE_ERROR("001","验证码错误"),
    PARAMS_LOST("002","传送参数丢失"),
    ALREADY_REGISTER("003","账号已注册"),
    SUCCESS("200","请求成功"),
    FAIL("201","请求错误|请求参数已失效"),
    PERMIT_REDIRECT("302","允许跳转，或需进行页面重定向"),
    SUCCESS_LOGIN("101", "登录成功");

    public String status;

    public String message;

    ResultCodeEnum(String status, String message){
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public static ResultCodeEnum getByStatus(String status){
        for(ResultCodeEnum code:ResultCodeEnum.values()){
            if(code.getStatus().equals(status)){
                return code;
            }
        }
        return null;
    }
}
