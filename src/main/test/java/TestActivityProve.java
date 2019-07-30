import com.Dream.dao.ActivityProveDao;
import com.Dream.entity.Activity;
import com.Dream.entity.ActivityProve;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.util.List;

@ContextConfiguration(locations = "classpath:spring-mybatis.xml")
public class TestActivityProve extends AbstractJUnit4SpringContextTests {

    @Autowired
    private ActivityProveDao activityProveDao;

    @Test
    public void testInsert(){
        ActivityProve activityProve = new ActivityProve();
        activityProve.setActivityId(2);
        activityProve.setActivityScore(2.0);
        activityProve.setStuClass("计算机一班");
        activityProve.setStuName("xxx");
        activityProve.setStuNum("201725010000");
        activityProve.setVolunTimeNum(2.0);
        activityProveDao.insert(activityProve);
    }

    @Test
    public void testUpdate(){
        ActivityProve activityProve = new ActivityProve();
        activityProve.setId(6);
        activityProve.setStuClass("计算机update班");
        activityProve.setStuNum("201734010000");
        activityProve.setVolunTimeNum(2.0);
        activityProveDao.update(activityProve);
    }

    @Test
    public void testDelete(){
        activityProveDao.delete(6);
    }

    @Test
    public void testDeleteByActID(){
        activityProveDao.deleteByActivityId(4);
    }

    @Test
    public void testSelectOne(){
        ActivityProve activityProve = new ActivityProve();
        activityProve.setActivityId(3);
        activityProve.setStuName("name4");
        ActivityProve result = activityProveDao.selectOne(activityProve);
        System.out.println(result.getStuName());
    }

    @Test
    public void testSelectByActivityId(){
        List<ActivityProve> list = activityProveDao.selectByActivityId(3);
        List<ActivityProve> list2 = activityProveDao.selectByStuName("name4");
        System.out.println();
    }
}
