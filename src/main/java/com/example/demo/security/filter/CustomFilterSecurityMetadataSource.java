package com.example.demo.security.filter;

import com.example.demo.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
public class CustomFilterSecurityMetadataSource implements FilterInvocationSecurityMetadataSource{

    //
    private final LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<>();

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) {
        final HttpServletRequest request = ((FilterInvocation) object).getRequest();

        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap
                .entrySet()) {
            if (entry.getKey().matches(request)) {
                return entry.getValue();
            }
        }

        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {

        Set<ConfigAttribute> allAttributes = new HashSet<>();
        Iterator iterator = this.requestMap.entrySet().iterator();

        while(iterator.hasNext()) {
            Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry = (Map.Entry)iterator.next();
            allAttributes.addAll(entry.getValue());
        }
        return allAttributes;
    }

    public void loadFromDatabaseMetadataSource() {
        log.info("==> loadFromDatabaseMetadataSource()");

        AntPathRequestMatcher resourceRequsetMatcher = new AntPathRequestMatcher("/vendor/**", "GET");
        requestMap.put(resourceRequsetMatcher, null); //resource 권한 permit all

        AntPathRequestMatcher adminPathRequestMatcher = new AntPathRequestMatcher("/admin/**", "GET");
        requestMap.put(adminPathRequestMatcher, SecurityConfig.createList("ADMIN"));

        AntPathRequestMatcher userPathRequestMatcher = new AntPathRequestMatcher("/user/**", "GET");
        requestMap.put(userPathRequestMatcher, SecurityConfig.createList("ADMIN", "USER"));
        log.info("<== loadFromDatabaseMetadataSource()");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}

