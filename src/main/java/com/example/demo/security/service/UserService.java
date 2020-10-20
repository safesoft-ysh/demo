package com.example.demo.security.service;

import com.example.demo.domain.Account;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {

    Account loadUserByUsername(String username) throws UsernameNotFoundException;

    Account setAuthorities(Account account);

}
