package dk.lundogbendsen.springbootcourse.rediscache.jpa;

import dk.lundogbendsen.springbootcourse.rediscache.jpa.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
