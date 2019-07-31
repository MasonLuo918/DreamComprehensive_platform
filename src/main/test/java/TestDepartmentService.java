import com.Dream.entity.Department;
import com.Dream.service.DepartmentService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
public class TestDepartmentService extends AbstractJUnit4SpringContextTests {
    @Autowired
    private DepartmentService departmentService;

    @Test
    public void test(){
        Department department = new Department();
        department.setDeptName("dlkaflkd");
        department.setCollege("dfal");
        department.setEmail("dlafjfld");
        department.setStatus(0);
        department.setPassword("daflndla");
        departmentService.insertDepartment(department);
    }
}
