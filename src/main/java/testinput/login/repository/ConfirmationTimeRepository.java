package testinput.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import testinput.login.entity.ConfirmationTime;

@Repository
public interface ConfirmationTimeRepository extends JpaRepository<ConfirmationTime, Integer> {
    
}