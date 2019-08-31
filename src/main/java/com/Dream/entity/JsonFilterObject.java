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

    private HashSet<String> hashSetForIncludes=new HashSet<>();

    private Map<Class, HashSet<String>> excludes=new HashMap<>();

    private HashSet<String> hashSetForExcludes=new HashSet<>();

    public HashSet<String> getHashSetForIncludes() {return hashSetForIncludes;}

    public HashSet<String> getHashSetForExcludes() {return hashSetForExcludes;}

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
