package com.example.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class SessionUtil extends WebUtils {

    /**
     * Request 얻어오기
     */
    public static HttpServletRequest getCurrentRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.
                currentRequestAttributes()).getRequest();
    }

    /**
     * Client IP 가져오기
     */
    public static String getClientIP(){
        String ip = getCurrentRequest().getHeader("X-Forwarded-For");
        //log.info(">>>>> X-FORWARDED-FOR : " + ip);

        if (ip == null) {
            ip = getCurrentRequest().getHeader("Proxy-Client-IP");
            //log.info(">>>> Proxy-Client-IP : " + ip);
        }
        if (ip == null) {
            ip = getCurrentRequest().getHeader("WL-Proxy-Client-IP"); // 웹로직
            //log.info(">>>> WL-Proxy-Client-IP : " + ip);
        }
        if (ip == null) {
            ip = getCurrentRequest().getHeader("HTTP_CLIENT_IP");
            //log.info(">>>> HTTP_CLIENT_IP : " + ip);
        }
        if (ip == null) {
            ip = getCurrentRequest().getHeader("HTTP_X_FORWARDED_FOR");
            //log.info(">>>> HTTP_X_FORWARDED_FOR : " + ip);
        }
        if (ip == null) {
            ip = getCurrentRequest().getRemoteAddr();
        }

        //log.info(">>>> Result : IP Address : " + ip);

        return ip;
    }

    /**
     * Client user ID가져오기
     */
    public static String getUserID(){
        return getCurrentRequest().getUserPrincipal().getName();
    }


}
