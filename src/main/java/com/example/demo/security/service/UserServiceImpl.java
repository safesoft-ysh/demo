package com.example.demo.security.service;

import com.example.demo.domain.Account;
import com.example.demo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final AccountRepository accountRepository;

    @Override
    public Account loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findByUserId(userId);
        if(optionalAccount.isPresent()) {
            Account account = optionalAccount.get();

            //Authorities Setting
            setAuthorities(account);

            return account;
        }
        throw new UsernameNotFoundException("[" + userId + "] User ID is Not Found");
    }

    @Override
    public Account setAuthorities(Account account) {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(account.getRole().getValue()));
        log.info("@SetAuthorities - User {}'s Auth : {}", account.getId(), authorityList);
        account.setAuthorities(authorityList);

        return account;
    }
}
