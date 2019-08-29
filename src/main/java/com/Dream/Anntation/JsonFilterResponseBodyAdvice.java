package com.Dream.Anntation;

import com.Dream.entity.JsonFilterObject;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Arrays;
import java.util.HashSet;

@ControllerAdvice
public class JsonFilterResponseBodyAdvice implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        JsonFilterObject jsonFilterObject=new JsonFilterObject();
        if(!methodParameter.getMethod().isAnnotationPresent(SerializeField.class)){
            return o;
        }
        if(methodParameter.getMethod().isAnnotationPresent(SerializeField.class)){
            Object object=methodParameter.getMethod().getAnnotation(SerializeField.class);
            handleAnnotation(SerializeField.class,object,jsonFilterObject);
        }
        return jsonFilterObject;
    }
    private void handleAnnotation(Class clazz, Object object, JsonFilterObject jsonFilterObject){

        String[] includes={};
        String[] excludes={};
        Class objClass=null;
        if(clazz.equals(SerializeField.class)){
            SerializeField serializeField=(SerializeField)object;
            includes=serializeField.includes();
            excludes=serializeField.excludes();
            objClass=serializeField.clazz();
        }
        if(includes.length>0){
            jsonFilterObject.getIncludes().put(objClass,new HashSet<String>(Arrays.asList(includes)));
        }else if(excludes.length>0){
            jsonFilterObject.getExcludes().put(objClass,new HashSet<String>(Arrays.asList(excludes)));
        }
    }
}
