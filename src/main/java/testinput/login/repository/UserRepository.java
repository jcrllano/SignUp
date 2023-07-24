package testinput.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import testinput.login.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
}