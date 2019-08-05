import com.Dream.dao.DepartmentDao;
import com.Dream.entity.Department;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
public class TestDepartmentDao extends AbstractJUnit4SpringContextTests {

    @Autowired
    private DepartmentDao departmentDao;

    @Test
    public void testUpdate(){
        List<Department> departments = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            Department department = new Department();
            department.setStatus(i % 2);
            department.setCreateTime(LocalDate.now().plusDays(i));
            department.setPassword("password" + i);
            department.setCollege("数学与信息学院");
            department.setDeptName("党务");
            department.setEmail("email" + i);
            departmentDao.insert(department);
            departments.add(department);
        }
        Department updateDepartment = departments.get(1);
        updateDepartment.setEmail("eamil " + "update");
        updateDepartment.setCollege("update");

        departmentDao.update(updateDepartment);
        for(Department department:departments){
            int id = department.getId();
            departmentDao.delete(id);
        }
    }

    @Test
    public void test(){
        Department department = new Department();
        department.setEmail("1066812978@qq.com");
        department.setPassword("69f3574f4f008accc1b31695e7551bb8");
        Department department1 = departmentDao.selectOne(department);
        System.out.println(department1);
    }
}
