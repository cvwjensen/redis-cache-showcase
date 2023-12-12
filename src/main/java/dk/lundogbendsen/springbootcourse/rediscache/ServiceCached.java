package dk.lundogbendsen.springbootcourse.rediscache;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ServiceCached {

    @Cacheable("isPrime")
    public boolean isPrime(long number) {
        System.out.println("Calculating isPrime(" + number + ")");
        if (number <= 1) {
            return false;
        }
        if (number == 2) {
            return true;
        }
        if (number % 2 == 0) {
            return false;
        }
        int maxDivisor = (int) Math.sqrt(number);
        for (int i = 3; i <= maxDivisor; i += 2) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}
