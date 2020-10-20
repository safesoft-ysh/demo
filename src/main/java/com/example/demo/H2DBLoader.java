package com.example.demo;

import com.example.demo.domain.Account;
import com.example.demo.domain.constant.UserRole;
import com.example.demo.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class H2DBLoader implements ApplicationRunner {

    private AccountRepository accountRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public H2DBLoader(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        log.info("@LoadUsers");
        Account admin = Account.builder()
                .id(1)
                .userId("admin")
                .userPw(passwordEncoder.encode("1"))
                .role(UserRole.ROLE_ADMIN).build();


        Account user = Account.builder()
                .id(2)
                .userId("user")
                .userPw(passwordEncoder.encode("1"))
                .role(UserRole.ROLE_USER).build();

        admin = accountRepository.save(admin);
        user = accountRepository.save(user);

        log.info("Admin : {}", admin);
        log.info("User : {}", user);
    }

}
