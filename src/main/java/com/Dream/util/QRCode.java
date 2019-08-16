package com.Dream.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import jdk.internal.util.xml.impl.Input;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.Buffer;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class QRCode {


    //使用zxing生成二维码
    public static InputStream createQRCode(String url,Map<String, String> map){
        //二维码的宽和高
        int width=300;
        int height=300;
        //二维码的格式
        String format="jpg";
        //二维码内容，扫描二维码输出到指定的网址
        StringBuilder builder=new StringBuilder();
        String mapkey=null;
        String mapValue=null;
        //String content="http://www.baidu.com?token=value&dkke";
        builder.append(url);
        builder.append("?");
        if(map!=null&&map.size()!=0){
            int size=map.size();
            int i=0;
            for(Map.Entry<String,String> entry:map.entrySet()){
                i++;
                mapkey=entry.getKey();
                mapValue=entry.getValue();
                if(i==size){
                    builder.append(mapkey+"="+mapValue);
                }else{
                    builder.append(mapkey+"="+mapValue+"&");
                }
            }
        }
        String content=builder.toString();


        //二维码的参数
        HashMap hints =new HashMap();
        //二维码内容编码
        hints.put(EncodeHintType.CHARACTER_SET,"UTF-8");
        //设置纠错等级
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        //设置边距
        hints.put(EncodeHintType.MARGIN,2);
        hints.put("message","success");
        BitMatrix bitMatrix=null;
        InputStream input=null;
        try{
            bitMatrix=new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE,width,height,hints);
            BufferedImage image=MatrixToImageWriter.toBufferedImage(bitMatrix);
            ByteArrayOutputStream os=new ByteArrayOutputStream();
            //输出二维码图片流
            try{
                ImageIO.write(image,format,os);
                input=new ByteArrayInputStream(os.toByteArray());
            }catch(IOException e){
                e.printStackTrace();
            }
//            try{
//                bitMatrix=new MultiFormatWriter().encode(content,BarcodeFormat.QR_CODE,width,height,hints);
//                Path file=new File("/Users/ynwu/desktop/123.png").toPath();
//                MatrixToImageWriter.writeToPath(bitMatrix,format,file);
//
//            }catch(Exception e){
//                e.printStackTrace();
//            }
            //MatrixToImageWriter.writeToStream(bitMatrix,format,outputStream);
        }catch(Exception e){
            e.printStackTrace();
        }
        return input;
    }

    //使用zxing解析二维码
    public static void ReadQRCode(File file){
        try{
            MultiFormatReader formatReader=new MultiFormatReader();
            BufferedImage image= ImageIO.read(file);
            BinaryBitmap binaryBitmap=new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
            HashMap hints=new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET,"UTF-8");
            Result result=formatReader.decode(binaryBitmap,hints);

            System.out.println("解析结果："+result.toString());
            System.out.println("解析格式："+result.getBarcodeFormat());
            System.out.println("解析内容："+result.getText());
        }catch(NotFoundException e){
            e.printStackTrace();
        }catch(IOException a){
            a.printStackTrace();
        }

    }

//    public static void main(String[] args) {
//        Map<String,String> map=new HashMap<>();
//        map.put("message","success");
//        map.put("fly","rich");
//        map.put("info","101");
//        QRCode.TestcreateQRCode("www.baidu.com",map);
//        QRCode.ReadQRCode(new File("/Users/ynwu/desktop/123.jpg"));
//    }

}