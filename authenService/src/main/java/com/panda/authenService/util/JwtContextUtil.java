package com.panda.authenService.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Slf4j
public class JwtContextUtil {

    /**
     * Get username from JWT context stored by JwtContextFilter
     */
    public static String getCurrentUsername() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                return (String) request.getAttribute("jwt.username");
            }
        } catch (Exception e) {
            log.error("Error getting current username from JWT context", e);
        }
        return null;
    }

    /**
     * Get original JWT token from context
     */
    public static String getCurrentJwtToken() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                return (String) request.getAttribute("jwt.token");
            }
        } catch (Exception e) {
            log.error("Error getting current JWT token from context", e);
        }
        return null;
    }

    /**
     * Check if current request has valid JWT context
     */
    public static boolean hasJwtContext() {
        return getCurrentUsername() != null && getCurrentJwtToken() != null;
    }

    /**
     * Get username from request header (added by gateway)
     */
    public static String getUsernameFromHeader() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                return request.getHeader("X-Username");
            }
        } catch (Exception e) {
            log.error("Error getting username from header", e);
        }
        return null;
    }
}
