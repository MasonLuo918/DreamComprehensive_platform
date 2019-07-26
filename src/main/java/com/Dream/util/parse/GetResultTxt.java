package com.Dream.util.parse;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class GetResultTxt {

    /**
     * 包含ocrid的一个url,返回最终链接
     *
     * @param url
     * @return
     */
    public static String getResult(String url) throws IOException, InterruptedException {
        String result = "";
        ObjectMapper objectMapper = new ObjectMapper();
        URL realURL = new URL(url);
        while(true){
            URLConnection conn = realURL.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");
            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += "\n" + line;
            }
            System.out.println(result);
            int start = result.indexOf("{");
            int end = result.lastIndexOf("}");
            result = result.substring(start, end + 1);
            Map<String, Object> resultMap = objectMapper.readValue(result, Map.class);
            if(resultMap.get("status").equals("2")){
                return (String) resultMap.get("content");
            }else{
                System.out.print(resultMap.get("status") + "  ");
                System.out.println("转换中...");
                Thread.sleep(2000);
            }
        }
    }

    public static String getTxt(String url){
        String result = "";
        try {
            URL realURL = new URL(url);
            URLConnection conn = realURL.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");
            conn.connect();
            Map<String, List<String>> map = conn.getHeaderFields();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += "\n" + line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
