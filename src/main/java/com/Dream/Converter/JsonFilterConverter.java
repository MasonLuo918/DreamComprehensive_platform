package com.Dream.Converter;

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

import static com.alibaba.fastjson.util.IOUtils.UTF8;

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
            JsonFilterObject jsonFilterObject = (JsonFilterObject) obj;
            OutputStream out = outputMessage.getBody();
            SimpleSerializeFilter simpleSerializeFilter = new SimpleSerializeFilter(jsonFilterObject.getIncludes(), jsonFilterObject.getExcludes());
            /**
             * JSON序列化接口toJSONString
             * String toJSONString(Object, SerializeFilter, SerializerFeature...)
             */
            String text = JSON.toJSONString(jsonFilterObject.getObject(), simpleSerializeFilter, features);
            byte[] bytes = text.getBytes(this.charset);
            out.write(bytes);
        } else {
            /**
             * 未声明@SerializeField注解
             */
            OutputStream out = outputMessage.getBody();
            String text = JSON.toJSONString(obj, this.features);
            byte[] bytes = text.getBytes(this.charset);
            out.write(bytes);
        }
    }

}
