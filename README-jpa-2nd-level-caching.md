# Hibernate second level cache with JPA and Redis

## Introduction
This project is a simple example of how to use Hibernate second level cache with JPA and Redis.

It uses an embedded database, H2, for persistence and Redis for second level caching.

The idea is to have Hibernate lookup in the cache before going to the database.

## Exercise 1: Add dependencies
Add the following dependencies to the pom.xml file:
- spring-boot-starter-data-jpa
- h2
- redisson-hibernate-53

This will enable support for JPA and H2 and Redisson for Hibernate second level cache.

### Solution

Add these snippets to the pom.xml file's dependencies section:

```
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.redisson</groupId>
        <artifactId>redisson-hibernate-53</artifactId>
        <version>3.19.0</version>
    </dependency>
```

## Exercise 2: Create the model

We need a Person class to work with. This is our "Model".

- Create a Person class with the following fields:
  - id
  - firstName
  - lastName

- Include getters and setters.
- And a default constructor.
- And a constructor that takes firstName and lastName as arguments.
- Make the Person class an entity by annotating it with @Entity.

### Solution

```
@Entity
public class Person {
    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;


    public Person() {
    }

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
```

## Exercise 3: Create the data repository for Person

Now we need a Spring Data repository for Person.

- Create an interface PersonRepository that extends JpaRepository<Person, Long>.

### Solution

```
public interface PersonRepository extends JpaRepository<Person, Long> {
}
```


## Exercise 4: Test the repository

We now have a standard JPA setup with an embedded database. No caching involved yet. But it is ready to be used.

- Create a test class PersonRepositoryTest annotated with the `@DataJpaTest` annotation.
- Inject the PersonRepository.
- Inject the EntityManager. This is required to clear the 1st level cache
- Create a test method that retrieves the Person using the PersonRepository, then clears the entityManager, and then repeat.
- Add the line `spring.jpa.show-sql=true` to the application.properties file. This enables logging of the SQL statements.
- See in the log that the Person is retrieved from the database twice. (2 select statements).

### Solution

``` java
@DataJpaTest
class RedisCacheApplicationTests {
    @Autowired
    PersonRepository personRepository;
    @Autowired
    EntityManager entityManager;
    @Test
    void retrievePersonTwiceTest() {
        Person person = new Person("Lars", "Larsen");
        personRepository.save(person);
        entityManager.clear();
        personRepository.findById(person.getId());
        entityManager.clear();
        personRepository.findById(person.getId());
    }
}
```

## Exercise 5: Add Redis as second level cache

Now we will add Redis as second level cache. This involves a redis configuration and a new annotation on the Person class.

- Add a file called redisson.yaml to the resources folder. The content of the file should be:
```
singleServerConfig:
    address: "redis://localhost:6379"
```

- Add the annotation `@Cache(region = "persons", usage = CacheConcurrencyStrategy.READ_WRITE)` to the Person class. This annotation tells Hibernate to use Redis as second level cache for the Person class.

### Solution

```
@Entity
@Cache(region = "persons", usage = CacheConcurrencyStrategy.READ_WRITE)
public class Person {
    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;


    public Person() {
    }

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

```


## Exercise 6: Update the test to use the second level cache

The DataJpaTest slice is not enabling second level caching. Therefore we need to add a few things to the test.

- Change the annotation on the test class to `@SpringBootTest`.
- Remove the EntityManager injection.
- Update the test to:

```java
    @Test
    void retrievePersonTwiceTest() {
        Person lars = personRepository.findById(1L).orElse(null);
        assertNull( lars);
        Person person = new Person("Lars", "Larsen");
        personRepository.save(person);
        lars = personRepository.findById(1L).orElse(null);
        assertNotNull( lars);
    }
```

- Run the test and see that it runs successfully.

To verify that the second level cache is used, we can look at the log. We should see that the Person is retrieved from the database once, and then from the cache the second time.
We should see a Cache miss from the first findById call, and a Cache hit from the second findById call. This proves that the seond level cache is working.

### Solution

```java
@SpringBootTest
class RedisCacheApplicationTests {

    @Autowired PersonRepository personRepository;
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
```

## Exercise 7: Run the test again - see that it no longer works

Repeat the unit test from exercise 6. It should fail. WHY?


## Exercise 8: Fix the test

The reason the test fails is that the second level cache is not cleared when we save a new Person. Therefore the second findById call will return the Person from the cache, even though it has not been saved to the database yet.

- Inject `@Autowired RedisTemplate redisTemplate;`. The redisTemplate is required to clear the second level cache before each test.
- Create a setup method annotated with `@BeforeEach`.
- In the setup method, use the redisTemplate to clear the cache using the command `redisTemplate.getConnectionFactory().getConnection().flushDb();`.
- Run the test again. It should now pass.
