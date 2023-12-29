package testinput.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import testinput.login.entity.ConfirmationToken;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, String> {
    ConfirmationToken findByConfirmationToken(String confirmationToken);
}
