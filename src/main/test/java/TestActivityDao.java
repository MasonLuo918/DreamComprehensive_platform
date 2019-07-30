import com.Dream.dao.ActivityDao;
import com.Dream.entity.Activity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import javax.ejb.Local;
import java.time.LocalDate;
import java.util.List;


@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
public class TestActivityDao extends AbstractJUnit4SpringContextTests {

    @Autowired
    private ActivityDao activityDao;

    @Test
    public void testInsert(){
        Activity activity = new Activity();
        activity.setName("test");
        activity.setTime(LocalDate.now());
        activityDao.insert(activity);
    }

    @Test
    public void testUpdate(){
        Activity activity = new Activity();
        activity.setId(6);
        activity.setName("test Update");
        activity.setTime(LocalDate.now());
        activityDao.update(activity);
    }

    @Test
    public void testDelete(){
        activityDao.delete(6);
    }

    @Test
    public void testSelectOne(){
        Activity activity = new Activity();
        activity.setId(2);
        Activity result = activityDao.selectOne(activity);
        System.out.println();
    }

    @Test
    public void testSelectByName(){
        List<Activity> list = activityDao.selectByName("活动");
        System.out.println();
    }

    @Test
    public void testSelectByTime(){
//        LocalDate startDate = LocalDate.of(2019,1,1);
//        LocalDate endDate = LocalDate.of(2019,7,30);
        List<Activity> list = activityDao.selectByDate(null, null);
        System.out.println();
    }
}
