package dk.lundogbendsen.springbootcourse.rediscache.jpa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(region = "persons", usage = CacheConcurrencyStrategy.READ_WRITE)
public class Person {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Long age;

    public Person(String name, Long age) {
        this.name = name;
        this.age = age;
    }

    public Person() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }
}
