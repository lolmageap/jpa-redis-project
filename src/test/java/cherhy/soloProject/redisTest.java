package cherhy.soloProject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
public class redisTest {

    @Autowired
    StringRedisTemplate redisTemplate;
    

    @Test
    public void testMember() throws Exception{
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String dummyValue = "apple";

        for (int i = 0; i < 1000; i++) {
            String key = String.format("%d", i);
            ops.set(key, dummyValue);
        }
    }    
    
}
