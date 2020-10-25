package com.example.demo.controller;

import com.example.demo.domain.Account;
import com.example.demo.domain.constant.UserRole;
import com.example.demo.repository.AccountRepository;
import com.example.demo.validator.AccountCheckValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@Slf4j
@SessionAttributes({"account", "message"})
@RequiredArgsConstructor
public class AdminController {

    //Account Repository
    private final AccountRepository accountRepository;

    //Account가 이미 있는지 Check 하기 위한 Validator
    private final AccountCheckValidator accountCheckValidator;

    //Password Encoder
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/admin")
    public String adminPage() {
        log.info("@Admin");

        return "admin";
    }

    @GetMapping("/admin/user/add")
    public String addUser(Model model) {
        log.info("@addUser");
        model.addAttribute("account", new Account());

        return "admin_useradd";
    }

    @PostMapping("/admin/user/addProc")
    public String addUserProc(Account account, Model model, BindingResult errors) {
        log.info("@AddUserProc");
        accountCheckValidator.validate(account, errors);

        //Password Encdoe 진행.
        account.setUserPw(passwordEncoder.encode(account.getUserPw()));

        //USER ROLE 세팅
        account.setRole(UserRole.ROLE_USER);

        if(errors.hasErrors()) {
            model.addAttribute("message", "이미 존재하는 User입니다.");
            model.addAttribute("account", account);
            return "admin_useradd";
        }
        else {
            Account savedAccount = accountRepository.save(account);
            log.info("저장된 account : {}", savedAccount);
        }

        return "redirect:/";
    }

}
