import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.util.concurrent.TimeUnit;

@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
public class TestRedisTemplate extends AbstractJUnit4SpringContextTests {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test(){
        redisTemplate.opsForValue().set("key1", "value1",60,TimeUnit.MINUTES);
    }
}
