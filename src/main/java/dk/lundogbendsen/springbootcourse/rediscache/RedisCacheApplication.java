package dk.lundogbendsen.springbootcourse.rediscache;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class RedisCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisCacheApplication.class, args);
    }


    @Bean
    CommandLineRunner runner(ServiceCached serviceCached) {
        return args -> {
            System.out.println("Is 2 prime? " + serviceCached.isPrime(2));
            System.out.println("Is 2 prime? " + serviceCached.isPrime(2));

            System.out.println("Is 3 prime? " + serviceCached.isPrime(3));
            System.out.println("Is 3 prime? " + serviceCached.isPrime(3));

            System.out.println("Is 4 prime? " + serviceCached.isPrime(4));
            System.out.println("Is 4 prime? " + serviceCached.isPrime(4));
            System.out.println("Is 2 prime? " + serviceCached.isPrime(920000009876035001L));
        };
    }
}
