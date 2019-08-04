package com.Dream.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class QRCode {
    //使用zxing生成二维码
    public static void createQRCode(OutputStream outputStream){
        //二维码的宽和高
        int width=300;
        int height=300;
        //二维码的格式
        String format="jpg";
        //二维码内容，扫描二维码输出到指定的网址
        String content="http://www.baidu.com";
        //二维码的参数
        HashMap hints =new HashMap();
        hints.put(EncodeHintType.CHARACTER_SET,"UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN,2);
        try{
            BitMatrix bitMatrix=new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE,width,height,hints);
            MatrixToImageWriter.writeToStream(bitMatrix,format,outputStream);
        }catch(Exception e){
            e.printStackTrace();
        }
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

}
