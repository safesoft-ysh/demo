package com.example.demo.domain.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum UserRole {
    ROLE_ADMIN("ADMIN"),
    ROLE_USER("USER");

    private String value;
}
