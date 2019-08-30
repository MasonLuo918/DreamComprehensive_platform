package com.Dream.util;


import com.Dream.Enum.OauthAccountEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.cxf.message.StringMap;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class URLUtils {


    private static String post(String url, Map<String, String> params) {
        PostMethod method = new PostMethod(url);
        String result = null;
        try {
            HttpClient httpClient = new HttpClient();
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
            httpClient.getHttpConnectionManager().getParams().setSoTimeout(3000);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                method.addParameter(key, value);
            }
            httpClient.executeMethod(method);
            byte[] response = method.getResponseBody();
            result = new String(response, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            method.releaseConnection();
        }
        return result;
    }

    /**
     * GET 请求
     *
     * @param url
     * @param params
     * @return 返回请求返回的数据
     */
    private static String get(String url, Map<String, String> params) {
        URL connURL = null;
        URLConnection conn = null;
        InputStream inputStream = null;
        url = getURL(url, params);
        StringBuilder result = new StringBuilder();
        try {
            connURL = new URL(url);
            conn = connURL.openConnection();
            inputStream = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = inputStream.read(buffer, 0, 1024)) > 0) {
                String temp = new String(buffer, 0, count, "UTF8");
                result.append(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    /**
     * 将url构造成get请求的url
     *
     * @param url
     * @param params
     * @return
     */
    public static String getURL(String url, Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return url;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(url);
        stringBuilder.append("?");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            stringBuilder.append(key + "=" + value);
            stringBuilder.append("&");
        }
        stringBuilder.deleteCharAt(stringBuilder.lastIndexOf("&"));
        return stringBuilder.toString();
    }

    /**
     * @param url 请求url
     * @param params 请求参数
     * @param requestMethod 请求方法,GET 或者 POST
     * @return
     */
    public static Map<String, String> doRequest(String url, Map<String, String > params, String requestMethod) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = "";
        if(requestMethod.equals("GET")){
            result = get(url, params);
        }else if(requestMethod.equals("POST")){
            result = post(url, params);
        }
        if(result != null){
            return objectMapper.readValue(result,Map.class);
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        String code = "fbdba28d7ceb51b86d7f103b21c004fc5662ff1f";
        String client_id = OauthAccountEnum.YIBAN.getAppID();
        String client_secret = OauthAccountEnum.YIBAN.getAppSecret();
        String redirect_uri = OauthAccountEnum.YIBAN.getRedirectURL();
        Map<String, String> params = new HashMap<>();
        params.put("code",code);
        params.put("client_id",client_id);
        params.put("client_secret",client_secret);
        params.put("redirect_uri",redirect_uri);
        System.out.println(post("https://openapi.yiban.cn/oauth/access_token", params));
    }
}
