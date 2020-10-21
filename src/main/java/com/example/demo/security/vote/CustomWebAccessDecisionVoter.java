package com.example.demo.security.vote;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@Slf4j
public class CustomWebAccessDecisionVoter implements AccessDecisionVoter<FilterInvocation> {


    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return configAttribute instanceof SecurityConfig;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass != null && aClass.isAssignableFrom(FilterInvocation.class);
    }

    @Override
    public int vote(Authentication authentication, FilterInvocation filterInvocation, Collection<ConfigAttribute> attributes) {
        log.info(">>>>>> AccessDecisionVoter - vote");

        if (authentication == null) {
            return -1;
        } else {
            boolean containAuthority;
            final int accessAuthoritySize = attributes.size();
            int containAuthorityCount = 0;
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            log.info("authorities Roles : {} ", authorities);

            //
            for (ConfigAttribute attribute : attributes) {
                if (this.supports(attribute)) {
                    for (GrantedAuthority authority : authorities) {
                        if (attribute.getAttribute().equals(authority.getAuthority())) {
                            containAuthorityCount++;
                            if (accessAuthoritySize == containAuthorityCount) {
                                break;
                            }
                        }
                    }
                }
            }

            //Request에 대한 Authorities 승인 여부 Check
            HttpServletRequest request = filterInvocation.getRequest();
            containAuthority = (containAuthorityCount > 0)? true : false;
            log.info("===> {}:[{}], Access Authorities:{}", authentication.getName(), request.getRequestURI(), attributes);
            log.info("<=== {}:[{}:{}], User Authorities:{} *** {} ***", authentication.getName(), request.getRequestURI(), request.getMethod(), authentication.getAuthorities(), (containAuthority ? "Access Granted." : "Access Denied."));
            log.info("<<<<<<< AccessDecisionVoter - vote");
            return containAuthority? ACCESS_GRANTED : ACCESS_DENIED;
        }
    }
}
