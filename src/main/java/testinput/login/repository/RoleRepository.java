package testinput.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import testinput.login.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
