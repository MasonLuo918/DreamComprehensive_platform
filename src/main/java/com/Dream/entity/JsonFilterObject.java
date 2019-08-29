package com.Dream.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/*
存储待处理的类以及要过滤的属性
 */

public class JsonFilterObject {

    private Object object;

    private Map<Class, HashSet<String>> includes=new HashMap<>();

    private Map<Class, HashSet<String>> excludes=new HashMap<>();

    public void setObject(Object object){
        this.object=object;
    }

    public Object getObject() {
        return object;
    }

    public Map<Class, HashSet<String>> getIncludes() {
        return includes;
    }

    public Map<Class, HashSet<String>> getExcludes() {
        return excludes;
    }
}
