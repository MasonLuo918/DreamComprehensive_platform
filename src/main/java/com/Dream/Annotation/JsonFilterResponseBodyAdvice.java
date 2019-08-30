package com.Dream.Annotation;

import com.Dream.entity.JsonFilterObject;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Arrays;
import java.util.HashSet;


/**
 * 对传递过来的数据提前进行处理，放到JsonFilterObject类中
 */
@ControllerAdvice
public class JsonFilterResponseBodyAdvice implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        JsonFilterObject jsonFilterObject=new JsonFilterObject();
        if(o == null){
            return o;
        }
        //没有SerializeField注解
        if((!methodParameter.getMethod().isAnnotationPresent(SerializeField.class))&&(!methodParameter.getMethod().isAnnotationPresent(SecSerializeField.class))){
            return o;
            //return JSONObject.toJSONString(o);
        }
        //有SerializeField注解
        if(methodParameter.getMethod().isAnnotationPresent(SerializeField.class)){
            Object object=methodParameter.getMethod().getAnnotation(SerializeField.class);
            handleAnnotation(SerializeField.class,object,jsonFilterObject);
        }
        if(methodParameter.getMethod().isAnnotationPresent(SecSerializeField.class)){
            Object object=methodParameter.getMethod().getAnnotation(SecSerializeField.class);
            handleAnnotation(SecSerializeField.class,object,jsonFilterObject);
        }
//        HashMap<Class,HashSet<String>> map=new HashMap<>();
//        map.put(o.getClass(),jsonFilterObject.getHashSet());
//        SimpleSerializeFilter simpleSerializeFilter=new SimpleSerializeFilter(map,null);
//        String result= JSONObject.toJSONString(o,simpleSerializeFilter);
//        System.out.println(result);
        jsonFilterObject.setObject(o);
        return jsonFilterObject;
    }

    /**
     *
     * @param clazz 注解的类
     * @param object 传送过来的类
     * @param jsonFilterObject 存储注解信息
     */
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
        if(clazz.equals(SecSerializeField.class)){
            SecSerializeField secSerializeField=(SecSerializeField)object;
            includes=secSerializeField.includes();
            excludes=secSerializeField.excludes();
            objClass=secSerializeField.clazz();
        }

        if(includes.length>0){
//            for(String string:includes){
//                System.out.println(string);
//            }
            for(String string:includes){
               jsonFilterObject.getHashSetForIncludes().add(string);
            }
            jsonFilterObject.getIncludes().put(objClass,new HashSet<String>(Arrays.asList(includes)));
        }else if(excludes.length>0){
            for(String string:excludes){
                jsonFilterObject.getHashSetForExcludes().add(string);
            }
            jsonFilterObject.getExcludes().put(objClass,new HashSet<String>(Arrays.asList(excludes)));
        }
    }
}
