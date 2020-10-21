package com.example.demo.security.config;

import com.example.demo.security.authentication.AccountAuthenticationProvider;
import com.example.demo.security.filter.CustomFilterSecurityMetadataSource;
import com.example.demo.security.intercepter.CustomFilterSecurityInterceptor;
import com.example.demo.security.service.CustomUserDetailsService;
import com.example.demo.security.vote.CustomWebAccessDecisionVoter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.DefaultWebInvocationPrivilegeEvaluator;
import org.springframework.security.web.access.WebInvocationPrivilegeEvaluator;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    //Thymeleaf Security namespace html에 항시 적용
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomUserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public AccountAuthenticationProvider accountAuthenticationProvider() {
        AccountAuthenticationProvider accountAuthenticationProvider = new AccountAuthenticationProvider();
        accountAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return accountAuthenticationProvider;
    }

    @Bean
    public CustomFilterSecurityMetadataSource customFilterSecurityMetadataSource() {
        CustomFilterSecurityMetadataSource customFilterSecurityMetadataSource = new CustomFilterSecurityMetadataSource();
        return customFilterSecurityMetadataSource;
    }

    @Bean
    public CustomFilterSecurityInterceptor customFilterSecurityInterceptor() throws Exception {
        CustomFilterSecurityInterceptor customFilterSecurityInterceptor = new CustomFilterSecurityInterceptor();
        customFilterSecurityInterceptor.setAccessDecisionManager(accessDecisionManager());
        customFilterSecurityInterceptor.setSecurityMetadataSource(customFilterSecurityMetadataSource());
        customFilterSecurityInterceptor.setAuthenticationManager(authenticationManagerBean());
        customFilterSecurityInterceptor.setObserveOncePerRequest(false);
        return customFilterSecurityInterceptor;
    }

    @Bean
    public WebInvocationPrivilegeEvaluator webInvocationPrivilegeEvaluator() throws Exception {
        return new DefaultWebInvocationPrivilegeEvaluator(customFilterSecurityInterceptor());
    }

    @Bean
    public DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler() {
        //Authorities 검증 시, 기본적으로 붙는 ROLE_을 없앰
        DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        defaultWebSecurityExpressionHandler.setDefaultRolePrefix("");
        return defaultWebSecurityExpressionHandler;
    }

    @Bean
    public CustomWebAccessDecisionVoter accessDecisonVoter() {
        CustomWebAccessDecisionVoter customAccessDecisonVoter = new CustomWebAccessDecisionVoter();
        return customAccessDecisonVoter;
    }

    @Bean
    public AffirmativeBased accessDecisionManager() {
        //AccessDecisionVoter에 대한 Class 정보를 받기 위한 List 생성
        List<AccessDecisionVoter<? extends Object>> accessDecisionVoters = new ArrayList<>();

        //Custom한 AccessDecisionVoter 추가
        accessDecisionVoters.add(accessDecisonVoter());

        //AffirmativeBased : 여러개의 Voter 중에서 하나라도 허용되면 허용
        AffirmativeBased affirmativeBased = new AffirmativeBased(accessDecisionVoters);
        //ConsensusBased : 다수결
        //ConsensusBased consensusBased = new ConsensusBased(accessDecisionVoters);
        //UnanimousBased : 만장일치
        //UnanimousBased unanimousBased = new UnanimousBased(accessDecisionVoters);

        return affirmativeBased;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //H2 Console을 사용하기 위한 옵션
        http.headers().frameOptions().disable();

        http.authorizeRequests()
                .antMatchers(
                        "/static/**",
                        "/error",
                        "/login",
                        "/console/**")
                .permitAll()
//                .antMatchers("/admin/**").hasAuthority("ADMIN")
//                .antMatchers("/user/**").hasAnyAuthority("ADMIN", "USER")
                .anyRequest().authenticated();

        http.formLogin()
                .loginPage("/login")
                .failureUrl("/login?error")
                .defaultSuccessUrl("/", true);


        http.logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID");

        http.csrf().disable();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(accountAuthenticationProvider())
                .userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.securityInterceptor(customFilterSecurityInterceptor())
        .privilegeEvaluator(webInvocationPrivilegeEvaluator())
        .expressionHandler(defaultWebSecurityExpressionHandler());
    }
}
