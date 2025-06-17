package com.panda.mdmService.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtContextFilter extends OncePerRequestFilter {

    private final SimpleJwtTokenProvider tokenProvider;

    public JwtContextFilter(SimpleJwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, 
                                  @NonNull HttpServletResponse response, 
                                  @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Get original JWT from header (added by gateway)
            String originalJwt = request.getHeader("X-Original-JWT");
            
            if (StringUtils.hasText(originalJwt) && tokenProvider.validateToken(originalJwt)) {
                String username = tokenProvider.getUsernameFromToken(originalJwt);
                log.debug("Storing JWT context for user: {}", username);
                
                // Store JWT info in request attributes for later use
                request.setAttribute("jwt.username", username);
                request.setAttribute("jwt.token", originalJwt);
                
                // You can also extract other claims if needed
                // request.setAttribute("jwt.roles", extractRoles(originalJwt));
            } else {
                log.debug("No valid original JWT found in request headers");
            }
        } catch (Exception ex) {
            log.error("Could not process JWT context", ex);
        }

        filterChain.doFilter(request, response);
    }
}
