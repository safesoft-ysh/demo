package com.example.demo.repository;

import com.example.demo.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    public Optional<Account> findByUserId(String userId);
}
