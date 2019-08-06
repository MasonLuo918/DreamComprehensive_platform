import com.Dream.dao.DepartmentDao;
import com.Dream.dao.SectionDao;
import com.Dream.entity.Department;
import com.Dream.entity.Section;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
public class TestDao extends AbstractJUnit4SpringContextTests {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private SectionDao sectionDao;

    @Test
    public void testDepartmentDaoUpdate(){
        List<Department> list = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            Department department = getTestDepartment(i);
            departmentDao.insert(department);
            list.add(department);
        }
        for(int i = 0; i < 10; i++){
            Department department = list.get(i);
            department.setDeptName("testDeptNameUpdate" + i);
            departmentDao.update(department);
        }
        for(int i = 0; i < 5; i++){
            Department department = list.get(i);
            departmentDao.delete(department.getId());
        }
    }

    @Test
    public void testDepartmentDaoSelect(){
        Department department = new Department();
        department.setEmail("1066812978@qq.com");
        department.setStatus(1);
        Department result = departmentDao.selectOne(department);
        System.out.println();
    }

    @Test
    public void testSectionSelect(){
        List<Section> list = sectionDao.selectByDepartmentID(9);
        System.out.println();
    }



    public Department getTestDepartment(int i){
        Department temp = new Department();
        temp.setEmail("testEmail" + i);
        temp.setPassword("testPassword" + i);
        temp.setStatus(i % 2);
        temp.setCreateTime(LocalDate.now());
        temp.setCollege("testCollege" + i);
        temp.setDeptName("testDeptName" + i);
        return temp;
    }


}



