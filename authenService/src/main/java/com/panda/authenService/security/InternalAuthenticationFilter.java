package com.panda.authenService.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
@Slf4j
public class InternalAuthenticationFilter extends OncePerRequestFilter {

    private final InternalTokenValidator internalTokenValidator;

    public InternalAuthenticationFilter(InternalTokenValidator internalTokenValidator) {
        this.internalTokenValidator = internalTokenValidator;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String internalToken = getInternalTokenFromRequest(request);

            if (StringUtils.hasText(internalToken) && internalTokenValidator.validateInternalToken(internalToken)) {
                String username = internalTokenValidator.extractUsername(internalToken);
                log.debug("Valid internal token found for user: {}", username);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_INTERNAL"))
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.debug("No valid internal token found");
            }
        } catch (Exception ex) {
            log.error("Could not set internal authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getInternalTokenFromRequest(HttpServletRequest request) {
        return request.getHeader("X-Internal-Auth");
    }
}