package com.Dream.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class FileUploadInterceptor implements HandlerInterceptor {

    private final String[] compressFileType = {"zip"};

    private final String[] docType = {"doc", "docx"};


    /**
     * 拦截文件上传的请求，只允许指定格式的文件上传，不成功，则返回如下例子：
     * {
     *     "act_doc": true,
     *     "material": false,
     *     "volun_Doc": true,
     *     "message": "文件类型错误,压缩文件仅允许.zip格式，doc文件只允许.doc 和 .docx",
     *     "status": "201"
     * }
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断是否为文件上传请求
        if(request instanceof MultipartHttpServletRequest){
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> resultMap = new HashMap<>();
            // 获取文件
            MultipartFile material =  multipartRequest.getFileMap().get("material");
            MultipartFile volunTimeDoc = multipartRequest.getFileMap().get("volun_doc");
            MultipartFile actDoc = multipartRequest.getFileMap().get("act_doc");
            boolean permitMaterial = isPermitType(material, compressFileType);
            boolean permitVolunDoc = isPermitType(volunTimeDoc, docType);
            boolean permitActDoc = isPermitType(actDoc, docType);
            if (permitMaterial && permitActDoc && permitVolunDoc) {
                return true;
            }else{

                response.setCharacterEncoding("utf-8");
                response.setHeader("Content-Type","application/json;charset=utf-8");
                resultMap.put("status","201");
                resultMap.put("material", permitMaterial);
                resultMap.put("volun_Doc", permitVolunDoc);
                resultMap.put("act_doc", permitActDoc);
                resultMap.put("message","文件类型错误,压缩文件仅允许.zip格式，doc文件只允许.doc 和 .docx");
                String result = objectMapper.writeValueAsString(resultMap);
                PrintWriter writer = response.getWriter();
                writer.write(result);
                writer.flush();
                response.flushBuffer();
                writer.close();
                return false;
            }
        }
        return true;
    }


    public boolean isPermitType(MultipartFile file, String[] types) {
        // 如果文件是空的，允许上传
        if (file == null) {
            return true;
        }
        String fileName = file.getOriginalFilename();
        int start = fileName.lastIndexOf(".") + 1;
        int end = fileName.length() - 1;
        String suffix = fileName.substring(start, end);
        for (String type : types) {
            if (suffix.equals(type)) {
                return true;
            }
        }
        return false;
    }
}
