package com.example.demo.controller;

import com.example.demo.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class LoginController {


    @GetMapping("/login")
    public String loginPage(@AuthenticationPrincipal Authentication authentication){

        log.info("User 권한 : {}", authentication);

        if(!ObjectUtils.isEmpty(authentication) && authentication.isAuthenticated()) {
            String ip = SessionUtil.getClientIP();
            log.info("Client IP : {}", ip);
            return "redirect:/";
        } else {
            return "login";
        }
    }


}
