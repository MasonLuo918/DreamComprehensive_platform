package java;
import com.Dream.service.ActivityProveParser;
import com.Dream.entity.UploadFile;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = "classpath:spring-mybatis.xml")
public class TestActivityProveParse extends AbstractJUnit4SpringContextTests {
    @Autowired
    private ActivityProveParser activityProveParser;

    @Test
    public void test(){
        UploadFile uploadFile = new UploadFile();
        uploadFile.setUuid("e18b49d14e26433fb2e7d73ef2556711");
        uploadFile.setPath("/Users/belle/Documents/MasonLuo/Idea/DreamComprehensive_platform/target/Dream.Comprehensive_platform/upload/college/depatNdaf/2019/09/22/material/e18b49d14e26433fb2e7d73ef2556711material.zip");
        uploadFile.setName("material.zip");
    }
}
