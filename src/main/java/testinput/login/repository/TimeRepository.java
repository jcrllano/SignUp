package testinput.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import testinput.login.entity.Time;

@Repository
public interface TimeRepository extends JpaRepository<Time, String> {
    
}
