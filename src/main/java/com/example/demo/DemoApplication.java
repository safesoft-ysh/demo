package com.example.demo;

import com.example.demo.security.filter.CustomFilterSecurityMetadataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@SpringBootApplication
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DemoApplication {

    private final CustomFilterSecurityMetadataSource customFilterSecurityMetadataSource;

    @PostConstruct
    public void afterPropertiesSet() {
        log.info("@After Properties Set");

        customFilterSecurityMetadataSource.loadFromDatabaseMetadataSource();
        log.info("===customFilterSecurityMetadataSource Processing");
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
