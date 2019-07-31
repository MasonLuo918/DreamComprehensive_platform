package com.Dream.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class ValidateCode {
    public static String generateCode(int width, int height, String imgType, OutputStream outputStream){
        //用来保存生成的验证码
        StringBuffer code = new StringBuffer();
        Random random = new Random();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.getColor("F8F8F8"));
        graphics.fillRect(0, 0,width,height);
        //生成随机线条的颜色
        Color[] colors = new Color[]{
                Color.BLUE, Color.GRAY, Color.GREEN, Color.RED, Color.BLACK, Color.ORANGE,
                Color.CYAN
        };
        //绘制干扰线
        for(int i = 0; i < 30; i++){
            graphics.setColor(colors[random.nextInt(colors.length)]);
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int w = random.nextInt(20);
            int h = random.nextInt(20);
            int signA = random.nextBoolean() ? 1 : -1;
            int signB = random.nextBoolean() ? 1 : -1;
            graphics.drawLine(x, y,  x + w * signA, y + h * signB);
        }
        graphics.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        //绘制字母
        for(int i = 0; i < 4; i++){
            //转换成ascii码
            int temp = random.nextInt(26) + 97;
            String s = String.valueOf((char) temp);
            code.append(s);
            graphics.setColor(colors[random.nextInt(colors.length)]);
            graphics.drawString(s, i * (width / 6), height - (height / 3));
        }
        //释放资源
        graphics.dispose();
        try{
            //写入输出流
            ImageIO.write(image, imgType, outputStream);
        }catch (IOException e){
            e.printStackTrace();
        }
        return code.toString();
    }
}
