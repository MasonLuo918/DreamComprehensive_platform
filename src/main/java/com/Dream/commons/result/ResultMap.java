package com.Dream.commons.result;

import java.util.HashMap;
import java.util.Map;

public class ResultMap extends Result<Map<String, Object>> {
    public ResultMap(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum);
    }

    public ResultMap(String status, String message) {
        super(status, message);
    }

    public ResultMap(ResultCodeEnum resultCodeEnum, Map data) {
        super(resultCodeEnum, data);
    }

    public ResultMap(Map data) {
        super(data);
    }

    public void put(String key, Object value){
        if(data == null){
            data = new HashMap<>();
        }
        data.put(key, value);
    }

    public Object remove(String key){
        if(data != null){
            Object value = data.remove(key);
            return value;
        }
        return null;
    }
}
