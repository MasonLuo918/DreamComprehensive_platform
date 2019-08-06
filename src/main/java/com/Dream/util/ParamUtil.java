package com.Dream.util;

import com.Dream.entity.Department;
import com.Dream.entity.Section;
import com.Dream.entity.type.UserType;

public class ParamUtil {
    public static boolean hasNull(Object ... args){
        for(Object arg:args){
            if(arg == null){
                return true;
            }
        }
        return false;
    }

    public static Integer getUserType(Object user){
        Integer userType = null;
        if(user instanceof Department){
            userType = UserType.DEPARTMENT;
        }
        if(user instanceof Section){
            userType = UserType.SECTION;
        }
        return userType;
    }
}
