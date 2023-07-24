package testinput.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import testinput.login.entity.Checking;

@Repository
public interface CheckingRepository extends JpaRepository<Checking, Integer> {
    
} 
