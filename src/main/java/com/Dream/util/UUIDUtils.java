package com.Dream.util;

import java.util.UUID;

public class UUIDUtils {
    //返回一个随机生成的UUID
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
