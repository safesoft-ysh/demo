package com.example.demo.domain;


import com.example.demo.domain.constant.UserRole;
import lombok.Builder;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Account implements UserDetails {

    public Account(Integer id) {this.id = id;}

    @Builder
    public Account(Integer id, String userId, String userPw, UserRole role) {
        this.id = id;
        this.userId = userId;
        this.userPw = userPw;
        this.role = role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "user_id", length = 30, unique = true)
    private String userId;

    @Column(name = "user_pw", length = 50)
    private String userPw;

    @Column(name = "account_non_locked")
    private boolean accountNonLocked;

    @Column(name = "enable")
    private boolean enable;

    @Setter
    @Column(nullable = false, length = 250)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Transient
    private boolean credentialsNonExpired;

    @Transient
    private List<GrantedAuthority> authorities = new ArrayList<>();


    @Override
    public Collection<GrantedAuthority> getAuthorities() {return authorities;}

    @Override
    public String getPassword() {
        return this.userPw;
    }

    @Override
    public String getUsername() {
        return this.userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enable;
    }

}
