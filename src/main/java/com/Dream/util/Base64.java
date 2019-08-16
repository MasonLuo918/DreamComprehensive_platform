package com.Dream.util;

import jdk.internal.util.xml.impl.Input;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

public class Base64 {
    public static String getImageStr(String imgFile) throws FileNotFoundException {
        return getImageStr(new File(imgFile));
    }

    public static String getImageStr(File imgFile) throws FileNotFoundException {
        return getImageStr(new FileInputStream(imgFile));
    }

    public static String getImageStr(InputStream inputStream){
        byte[] data = null;
        try{
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

    public static boolean generateImage(String base64str, String savePath){
        if(base64str == null){
            return false;
        }
        BASE64Decoder base64Decoder = new BASE64Decoder();
        try{
            byte[] b = base64Decoder.decodeBuffer(base64str);
            for(int i = 0; i < b.length; i++){
                if(b[i] < 0){
                    b[i] += 256;
                }
            }
            OutputStream outputStream = new FileOutputStream(savePath);
            outputStream.write(b);
            outputStream.flush();
            outputStream.close();
            return true;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args){
        String str = getImageStr("/Users/belle/Desktop/material/u407.jpg");
        System.out.println(str.length());
    }
}
