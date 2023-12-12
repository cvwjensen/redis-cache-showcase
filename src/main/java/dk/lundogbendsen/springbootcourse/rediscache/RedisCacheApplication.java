package dk.lundogbendsen.springbootcourse.rediscache;

import dk.lundogbendsen.springbootcourse.rediscache.jpa.PersonRepository;
import dk.lundogbendsen.springbootcourse.rediscache.jpa.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

@SpringBootApplication
public class RedisCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisCacheApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(PersonRepository personRepository) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(RedisCacheApplication.class);
            logger.info("LOOKING FOR PERSON WITH ID 1");
            Optional<Person> person = personRepository.findById(1L);
            logger.info("LOOKING FOR PERSON WITH ID 666");
            person = personRepository.findById(666L);
            logger.info("SAVING PERSON WITH ID 1");
            Person lars = personRepository.save(new Person("Lars", 21L));
            logger.info("LOOKING FOR PERSON WITH ID 1 AGAIN");
            person = personRepository.findById(1L);
            logger.info("lars.getName() = " + person.get().getName());
        };
    }

}
