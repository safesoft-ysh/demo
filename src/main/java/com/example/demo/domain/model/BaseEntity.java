package com.example.demo.domain.model;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDateTime;

public class BaseEntity {

    @CreatedBy
    private LocalDateTime createDate;

    @LastModifiedBy
    private LocalDateTime updateDate;


}
