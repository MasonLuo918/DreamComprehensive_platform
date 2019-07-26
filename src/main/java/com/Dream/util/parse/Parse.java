package com.Dream.util.parse;

import com.Dream.entity.VolunteerTime;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parse {
    public static String pattern = ".*([0-9]{2}级.*[0-9]班)\\s*(.*)\\s*([0-9]{12})\\s*([0-9]小时).*";
    public static List<VolunteerTime> parse(String[] result){
        List<VolunteerTime> list = new ArrayList<>();
        Pattern pattern1 = Pattern.compile(pattern);
        for(String s:result){
            Matcher matcher = pattern1.matcher(s);
            if(matcher.find()){
                VolunteerTime tempTime = new VolunteerTime();
                tempTime.set_class(matcher.group(1));
                tempTime.set_name(matcher.group(2));
                tempTime.set_studentNum(matcher.group(3));
                tempTime.set_times(matcher.group(4));
                list.add(tempTime);
            }
        }
        return list;
    }

}
