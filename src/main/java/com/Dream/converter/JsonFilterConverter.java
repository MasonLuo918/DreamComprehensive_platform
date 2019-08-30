package com.Dream.converter;

import com.Dream.entity.JsonFilterObject;
import com.Dream.filiter.SimpleSerializeFilter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static com.alibaba.fastjson.util.IOUtils.UTF8;

/**
 * FastJson转换器
 */
public class JsonFilterConverter extends FastJsonHttpMessageConverter {

    private Charset charset;
    private SerializerFeature[] features;

    public JsonFilterConverter() {
        super();
        setSupportedMediaTypes(Arrays.asList(
                new MediaType("application", "json", UTF8),
                new MediaType("application", "*+json", UTF8),
                new MediaType("application", "jsonp", UTF8),
                new MediaType("application", "*+jsonp", UTF8)));
        setCharset(UTF8);
        setFeatures(SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue);
    }

    @Override
    protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        if (obj instanceof JsonFilterObject) {
            /**
             * 声明了@SerializeField注解
             */
            JsonFilterObject jsonFilterObject = (JsonFilterObject) obj;
            OutputStream out = outputMessage.getBody();
            //SimpleSerializeFilter simpleSerializeFilter = new SimpleSerializeFilter(jsonFilterObject.getIncludes(), jsonFilterObject.getExcludes());
            /**
             * JSON序列化接口toJSONString
             * String toJSONString(Object, SerializeFilter, SerializerFeature...)
             */
            HashMap<Class, HashSet<String>> map=new HashMap<>();
            if(jsonFilterObject.getHashSetForIncludes().size()!=0){
                /**
                 * 声明了includes
                 */
                //map.put(jsonFilterObject.getObject().getClass(),jsonFilterObject.getHashSetForIncludes());
                //SimpleSerializeFilter simpleSerializeFilter=new SimpleSerializeFilter(map,null);
                SimpleSerializeFilter simpleSerializeFilter=new SimpleSerializeFilter(jsonFilterObject.getIncludes(),null);
                String text = JSON.toJSONString(jsonFilterObject.getObject(), simpleSerializeFilter);
                //System.out.println(text);
                byte[] bytes = text.getBytes(this.charset);
                out.write(bytes);
            }else if(jsonFilterObject.getHashSetForExcludes().size()!=0){
                /**
                 * 声明了excludes
                 */
                //map.put(jsonFilterObject.getObject().getClass(),jsonFilterObject.getHashSetForExcludes());
                //SimpleSerializeFilter simpleSerializeFilter=new SimpleSerializeFilter(null,map);
                SimpleSerializeFilter simpleSerializeFilter=new SimpleSerializeFilter(null,jsonFilterObject.getExcludes());
                String text=JSON.toJSONString(jsonFilterObject.getObject(),simpleSerializeFilter);
                //System.out.println(text);
                byte[] bytes=text.getBytes(this.charset);
                out.write(bytes);
            }
        } else {
            /**
             * 未声明@SerializeField注解
             */
            OutputStream out = outputMessage.getBody();
            String text = JSON.toJSONString(obj, this.features);
            byte[] bytes = text.getBytes(this.charset);
            System.out.println(text);
            out.write(bytes);
        }
    }
    @Override
    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    @Override
    public void setFeatures(SerializerFeature... features) {
        this.features = features;
    }
}
