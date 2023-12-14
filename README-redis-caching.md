# Caching with Redis

## What is Redis?
Redis is a memory database. It is used to store data in memory, which makes it very fast. 
It is also a key-value store, which means that you can store data in Redis using a key, and then retrieve it using the same key. 
Redis is often used as a cache, because of its speed and simplicity.

## Caching in SpringBoot
SpringBoot has a very simple way of adding caching to your application. 
You simply add the `@EnableCaching` annotation to your configuration class, and SpringBoot will automatically create a cache for you. 
You can then use the `@Cacheable` annotation to cache the result of a method.

## Exercise 1: Create a ServiceCached class for showcasing caching

- Create a class `ServiceCached` that implements calculation of Fibonacci numbers or determinting if a number is prime.
- Add a method to the service that calculates the Fibonacci number for a given number.
- (OR: Add a method to the service that determines if a given number is prime.)
- Add a print statement at the beginning of the method printing which number is being calculated
- Create a restcontroller that exposes a Get method receiving a number, autowires the service and calls the method with the number. 
- Test the restcontroller by calling the method with the number 13. Notice in the logs that the method is called.
  - `curl "localhost:8080/fibonacci?n=13"`


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
@RestController
public class RedisController {

    @Autowired ServiceCached serviceCached;

    @GetMapping("/fibonacci")
    public int fibonacci(@RequestParam int n) {
        return serviceCached.fibonacci(n);
    }
}
```

## Exercise 2: Add caching to the ServiceCached class
- Add the `@EnableCaching` annotation to the configuration class.
- Add the `@Cacheable("fibonacci")` annotation to the method that calculates the Fibonacci number (or prime). This will cache the result of the method in a cache called "fibonacci".
- Restart the app and use the api to call the method that calculates the Fibonacci number (or prime) with the number 13. Notice in the logs that the method is called.
- Call the method again with the same number.Notice that the method does not print anything for the second calculation. This is because the method is not called, but the result is retrieved from the cache.


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

Just by added Redis to the classpath, SpringBoot will automatically use Redis as the cache.

- Stop the application if it is running.
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
 
- Restart the application and call the method that calculates the Fibonacci number (or prime) with the number 13.
- Notice in the logs that the method is called.
- In a browser, inspect Redis by going to http://localhost:8001/ and connect to the already running Redis server using:
  - Host: redis
  - Port: 6379
  - Name: springbootcourse

- Stop the application and start it again. Call the method again with the same number and print the result. Notice that the method is not called again, but the result is retrieved from the cache. This is because the cache is stored in Redis, and is not cleared when the application is restarted.


## Exercise 5: Improve Redis serialization

We notice that the default serialization of the cache is not very readable.
It looks something like this:

```
{
  "classdesc": {
    "name": "java.lang.Integer",
    "serialVersionUID": 1360826667806852900,
    "flags": 2,
    "fields_names": [
      "value"
    ],
    "fields_types": [
      "I"
    ],
    "superclass": {
      "name": "java.lang.Number",
      "serialVersionUID": -8742448824652079000,
      "flags": 2,
      "fields_names": [],
      "fields_types": [],
      "superclass": null
    }
  },
  "annotations": [],
  "value": 377
}
```

This is how javas builtin serialization works.
This is how an Integer looks.

We can improve this by adding a serializer to the cache. 

- Create a class RedisConfiguration.
- Add the `@Configuration` annotation to the class.
- Create a method `RedisCacheConfiguration redisCacheConfiguration()` that returns a `RedisCacheConfiguration` object:
```java
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }
```

- Go to the browser and delete any existing keys in Redis. (eg click on the key and then the delete button to the far right)
- Restart the application and call the method that calculates the Fibonacci number (or prime) with the number 13.
- Notice how the serialization is now much more readable.
