import com.Dream.dao.SectionDao;
import com.Dream.entity.Section;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.util.List;

@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
public class TestSectionDao extends AbstractJUnit4SpringContextTests {

    @Autowired
    private SectionDao sectionDao;


    @Test
    public void testInsert(){
        Section section = new Section();
        section.setAccount("test");
        section.setDepartmentID(1);
        section.setName("test");
        section.setPassword("test");
        sectionDao.insert(section);
    }

    @Test
    public void testUpdate(){
        Section section = new Section();
        section.setId(6);
        section.setPassword("updatePassword");
        section.setName("updateName");
        sectionDao.update(section);
    }

    @Test
    public void testDelete(){
        sectionDao.delete(10);
    }

    @Test
    public void testDeleteByAccount(){
        sectionDao.deleteByAccount("test1");
    }

    @Test
    public void testSelectOne(){
        Section section1 = new Section();
        section1.setDepartmentID(5);
        Section section = sectionDao.selectOne(section1);
        System.out.println(section);
    }

    @Test
    public void testSelectByDepartment(){
        List<Section> list = sectionDao.selectByDepartmentID(5);
        System.out.println();
    }

    @Test
    public void testSelectByAccount(){
        Section section = sectionDao.selectByAccount("test");
        System.out.println();
    }
}
