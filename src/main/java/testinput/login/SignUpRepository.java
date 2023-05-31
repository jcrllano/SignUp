package testinput.login;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SignUpRepository extends JpaRepository<SignUpForm, String> {
    
}
