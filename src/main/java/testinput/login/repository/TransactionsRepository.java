package testinput.login.repository;

import java.util.List;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.transaction.Transaction;
import testinput.login.entity.Transactions;

public interface TransactionsRepository extends JpaRepository<Transactions, Integer> {
    
}
