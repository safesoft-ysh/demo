package com.example.demo.validator;

import com.example.demo.domain.Account;
import com.example.demo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccountCheckValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Account account = (Account) target;

        Optional<Account> accountOptional = accountRepository.findByUserId(account.getUserId());

        if(accountOptional.isPresent()) {
            errors.reject("message.account.already.used", "이미 사용중인 User ID 입니다.");
        }
    }
}
