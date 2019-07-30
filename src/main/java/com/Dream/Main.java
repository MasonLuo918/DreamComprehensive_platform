package com.Dream;

import com.Dream.dao.DepartmentDao;
import com.Dream.dao.SectionDao;
import com.Dream.entity.Department;
import com.Dream.entity.Section;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.time.LocalDate;
import java.util.Date;

public class Main {
    public static void main(String[] args){
        LocalDate localDate = LocalDate.of(2017, 2,1);
        System.out.println(localDate);
    }
}
