# Caching with Redis

## What is Redis?
Redis is a memory database. It is used to store data in memory, which makes it very fast. It is also a key-value store, which means that you can store data in Redis using a key, and then retrieve it using the same key. Redis is often used as a cache, because of its speed and simplicity.

## Caching in SpringBoot
SpringBoot has a very simple way of adding caching to your application. You simply add the `@EnableCaching` annotation to your configuration class, and SpringBoot will automatically create a cache for you. You can then use the `@Cacheable` annotation to cache the result of a method.

## Exercise 1: Create a ServiceCached class for showcasing caching

- Create a class `ServiceCached` that implements calculation of Fibonacci numbers or determinting if a number is prime.
- Add a method to the service that calculates the Fibonacci number for a given number.
- (OR: Add a method to the service that determines if a given number is prime.)
- Add a print statement at the beginning of the method printing which number is being calculated
- Test the method by calling it with a number and printing the result. You can use the CommandLineRunner to setup a development test

### Solution
```
@Service
public class ServiceCached {
    public int fibonacci(int n) {
        System.out.println("Calculating fibonacci number for " + n);
        if (n <= 1) {
            return n;
        }
        return fibonacci(n - 1) + fibonacci(n - 2);
    }
}
```

```
    @Bean
    public CommandLineRunner runner(ServiceCached serviceCached) {
        return args -> {
            System.out.println("serviceCached = " + serviceCached.getClass());
            System.out.println("Test your service in this scope...");
        };
    }

```

## Exercise 2: Add caching to the ServiceCached class
- Add the `@EnableCaching` annotation to the configuration class.
- Add the `@Cacheable` annotation to the method that calculates the Fibonacci number (or prime).
- Test the method by calling it with the number 13 and printing the result.
- Call the method again with the same number and print the result. Notice that the method is not called again, but the result is retrieved from the cache.
- Notice that the method does not print anything for the second calculation. This is because the method is not called, but the result is retrieved from the cache.


### Solution
```
@Service
public class ServiceCached {
    @Cacheable("fibonacci")
    public int fibonacci(int n) {
        System.out.println("Calculating fibonacci number for " + n);
        if (n <= 1) {
            return n;
        }
        return fibonacci(n - 1) + fibonacci(n - 2);
    }
}
```


## Exercise 3: Add Redis to the project
- Stop the application, if it is running.
- Add the `spring-boot-starter-data-redis` dependency to the pom.xml file.
- The project already comes with a docker-compose file for starting a Redis server. Start the Redis server by running `docker-compose up` in the root of the project.
- As an alternative, add the docker-compose support dependency to the pom.xml file to control docker-compose when you start the application.

### Solution
```
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-docker-compose</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
    </dependency>
``` 

## Exercise 4: Test the caching with Redis
- Run the application and call the method that calculates the Fibonacci number (or prime) with the number 13.
- Notice that the method is not called again, but the result is retrieved from the cache.
- Stop the application and start it again. Call the method again with the same number and print the result. Notice that the method is not called again, but the result is retrieved from the cache. This is because the cache is stored in Redis, and is not cleared when the application is restarted.

## Exercise 5: Inspect Redis
- Start the Redis server by running `docker-compose up` in the root of the project.
- Open a Browser on http://localhost:8001/ to inspect the Redis server.
- Connect to the already running Redis server using:
  - Host: redis
  - Port: 6379
  - Name: springbootcourse
- Inspect the keys in the database. Notice that there is a key called `fibonacci::13` with the value `233`.




