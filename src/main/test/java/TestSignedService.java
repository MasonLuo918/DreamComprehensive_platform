import com.Dream.bean.TokenProperty;
import com.Dream.commons.cache.Cache;
import com.Dream.service.SignedInService;
import com.Dream.service.impl.SignedInServiceImpl;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
public class TestSignedService extends AbstractJUnit4SpringContextTests {
    @Autowired
    private SignedInServiceImpl singedInService;


    @Test
    public void testStartActivity() throws InterruptedException {
        TokenProperty tokenProperty = new TokenProperty();
        tokenProperty.setId(1);
        tokenProperty.setValidity(5);
        String token = singedInService.start(tokenProperty);
        System.out.println(Cache.get(token));
        Thread.sleep(5000);
        System.out.println(Cache.get(token));
    }

    @Test
    public void testInsert(){
        singedInService.dealWithTokenExpire("be1d2159ed800a5f8d97cee246b5d74e");
    }
}
