package com.Dream.commons.result;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    protected T data;

    protected String status;

    protected String message;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Result(ResultCodeEnum resultCodeEnum){
        this.status = resultCodeEnum.getStatus();
        this.message = resultCodeEnum.getMessage();
    }

    public Result(String status, String message){
        this.status = status;
        this.message = message;
    }

    public Result(ResultCodeEnum resultCodeEnum, T data){
        this.data = data;
        this.status = resultCodeEnum.getStatus();
        this.message = resultCodeEnum.getMessage();
    }

    public Result(T data){
        this(ResultCodeEnum.SUCCESS, data);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
