import com.Dream.dao.DepartmentDao;
import com.Dream.entity.Department;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import javax.annotation.Resource;
import java.util.List;

@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
public class TestDepartmentDao extends AbstractJUnit4SpringContextTests {

    @Autowired
    private DepartmentDao departmentDao;

    @Test
    public void testDelete(){
        departmentDao.delete(4);
    }

    @Test
    public void testDeleteByEmail(){
        departmentDao.deleteByEmail("mason@qq.com");
    }

    @Test
    public void testUpdate(){
        Department department = new Department();
        department.setId(2);
        department.setDeptName("学生党务管理委员会");
        departmentDao.update(department);
    }

    @Test
    public void testSelect(){
        //测试根据字段查找department
        Department department = new Department();
        department.setId(5);
        Department result = departmentDao.selectOne(department);
        Department resultEamil = departmentDao.selectByEmail("mason luo918@gamil.com");
        List<Department> listByCollege = departmentDao.selectByCollege("数学与信息学院");
        List<Department> listBydeptName = departmentDao.selectByDepartmentName("学生党务管理委员会");
        System.out.println();
    }
}
