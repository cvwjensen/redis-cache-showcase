package dk.lundogbendsen.springbootcourse.rediscache;

import dk.lundogbendsen.springbootcourse.rediscache.jpa.PersonRepository;
import dk.lundogbendsen.springbootcourse.rediscache.jpa.model.Person;
import jakarta.persistence.EntityManager;
import org.hibernate.Cache;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.AutoConfigureDataRedis;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

//@DataJpaTest(properties = {"spring.jpa.properties.javax.persistence.sharedCache.mode=ENABLE_SELECTIVE"})
//@AutoConfigureDataRedis
@SpringBootTest
class RedisCacheApplicationTests {

    @Autowired
    PersonRepository personRepository;
    @Autowired RedisTemplate redisTemplate;
    @BeforeEach
    public void setup() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }
    @Test
    void retrievePersonTwiceTest() {
        Person lars = personRepository.findById(1L).orElse(null);
        assertNull( lars);
        Person person = new Person("Lars", "Larsen");
        personRepository.save(person);
        lars = personRepository.findById(1L).orElse(null);
        assertNotNull( lars);
    }
}
