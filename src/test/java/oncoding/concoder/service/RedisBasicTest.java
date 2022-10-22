package oncoding.concoder.service;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.concurrent.TimeUnit;
import javax.persistence.Entity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;

/*
@SpringBootTest
@SpringBootApplication 애노테이션을 찾아가서 이 애노테이션부터 시작하는 모든 빈을 스캔하는 것이다.
그리고 스캔한 모든 빈을 테스트용 애플리케이션에 다 등록해주는 것
 */

@SpringBootTest
//@ActiveProfiles("test")
//@AutoConfigureTestDatabase(replace = Replace.AUTO_CONFIGURED) // 실제 DB 사용하고 싶을때 NONE 사용
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class RedisBasicTest {
    
    @Autowired
    RedisTemplate<String, String> redisTemplate;
    
 
    public static class RedisUserDto{
        private String name;
        private String password;
        
        public RedisUserDto(String name, String password){
            this.name = name;
            this.password = password;
        }
    
        public RedisUserDto() {
        
        }
    
        public String getName(){
            return this.name;
        }

        public String getPassword(){
            return this.password;
        }
        
    }
    
    /**
     * 단순 연결 테스트 -  key와 value 삽입
     */
    @Test
    void redisConnectionTest() {
        final String key = "a";
        final String data = "1";
        
        final ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, data);
        
        final String s = valueOperations.get(key);
        Assertions.assertThat(s).isEqualTo(data);
    }
    
    @Test
    void redisInsertObject(){
        RedisUserDto redisUserDto = new RedisUserDto("kenux", "password");
        
        final ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(redisUserDto.getName(), String.valueOf(redisUserDto));
    
        final String result = valueOperations.get(redisUserDto.getName());

        System.out.println("result = " + result);
    
    }
    
    /**
     * 위 코드 redis에 삽입한 아이템에 대해서 5초의 expire time을 설정하고, 5초가 지난 후에 redis에서 해당 키가 조회되는지 테스트하는 코드
     * @throws InterruptedException
     */
    @Test
    void redisExpireTest() throws InterruptedException {
        final String key = "a";
        final String data = "1";
        
        final ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, data);
        final Boolean expire = redisTemplate.expire(key, 5, TimeUnit.SECONDS);
        Thread.sleep(6000);
        final String s = valueOperations.get(key);
//        assertThat(expire).isTrue();
//        assertThat(s).isNull();
    }
    
}
