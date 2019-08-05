package com.Dream.util.parse.docparse;

import com.Dream.entity.Activity;
import com.Dream.entity.ActivityProve;
import org.apache.poi.hwpf.usermodel.Paragraph;

import javax.print.Doc;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractDocParser implements DocParser {

    public void setProperty(ActivityProve prove, String value, int type){
        switch (type){
            case 0:
                setStuClass(prove, value);
                break;
            case 1:
                setStuNum(prove, value);
                break;
            case 2:
                setStuName(prove, value);
                break;
            case 3:
                setStuVolunTime(prove, value);
                break;
        }
    }

    private void setStuNum(ActivityProve prove, String stuNum){
        if(prove == null || stuNum == null){
            return;
        }
        prove.setStuNum(stuNum);
    }

    private void setStuClass(ActivityProve prove, String stuClass){
        if(prove == null || stuClass == null){
            return;
        }
        prove.setStuClass(stuClass);
    }

    private void setStuName(ActivityProve prove, String stuName){
        if(prove == null || stuName == null){
            return;
        }
        prove.setStuName(stuName);
    }

    private void setStuVolunTime(ActivityProve prove, String volunTime){
        if(prove == null || volunTime == null){
            return;
        }
        Pattern pattern = Pattern.compile("\\d+\\.?\\d*");
        Matcher matcher = pattern.matcher(volunTime);
        double times = 0;
        while(matcher.find()){
            String str = matcher.group();
            times += Double.valueOf(str);
        }
        prove.setVolunTimeNum(times);
    }
}
