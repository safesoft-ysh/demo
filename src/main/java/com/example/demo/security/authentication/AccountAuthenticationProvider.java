package com.example.demo.security.authentication;

import com.example.demo.domain.Account;
import com.example.demo.security.service.CustomUserDetailsService;
import com.example.demo.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
public class AccountAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private PasswordEncoder passwordEncoder;

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        Object credentials = authentication.getCredentials();
        Account account = customUserDetailsService.loadUserByUsername(username);

        log.info("Account Roles : {}", account.getAuthorities());

        String password = (String) credentials;
        passwordMatches(password, account);

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(account, account.getPassword(), account.getAuthorities());



        return token;
    }

    protected void passwordMatches(String password, Account account) throws BadCredentialsException {
        if(!passwordEncoder.matches(password, account.getPassword())) {
            throw new BadCredentialsException("Password is not match");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
