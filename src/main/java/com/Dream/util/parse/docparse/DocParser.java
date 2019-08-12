package com.Dream.util.parse.docparse;

import com.Dream.entity.ActivityProve;

import java.io.File;
import java.util.List;

public interface DocParser {

    final String NAME = "姓名";

    final String CLASS = "班级";

    final String VOLUNTEER_TIME = "志愿时";

    final String STUDENT_NUM = "学号";

    /**
     * 获取一个文档的活动证明,type表示类型,志愿时公式或者是活动分公式
     * 1代表志愿时，0代表活动分
     * @return
     */
    List<ActivityProve> getResult(String filePath, Integer activityID);
}
