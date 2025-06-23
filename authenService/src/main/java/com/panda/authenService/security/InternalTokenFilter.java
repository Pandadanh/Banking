package com.panda.authenService.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Key;
import java.util.Base64;

@Component
@Slf4j
public class InternalTokenFilter extends OncePerRequestFilter {

    @Value("${security.jwt.internal-secret-key}")
    private String internalSecretKey;

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(internalSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        String internalToken = request.getHeader("X-Internal-Auth");
        String originalJwt = request.getHeader("X-Original-JWT");
        String username = request.getHeader("X-Username");

        log.debug("Processing request with internal auth: {}", internalToken != null ? "Present" : "Missing");
        log.debug("Original JWT: {}, Username: {}", originalJwt != null ? "Present" : "Missing", username);
        
        // Skip validation for health check and public endpoints
        String requestPath = request.getRequestURI();
        if (isPublicEndpoint(requestPath)) {
            log.debug("Skipping internal token validation for public endpoint: {}", requestPath);
            filterChain.doFilter(request, response);
            return;
        }
        
        // Validate internal token if present
        if (internalToken != null) {
            if (validateInternalToken(internalToken)) {
                log.debug("Internal token validation successful");

                // Add original JWT and username to request attributes
                if (originalJwt != null && !originalJwt.isEmpty()) {
                    request.setAttribute("originalJwt", originalJwt);
                    log.debug("Original JWT added to request attributes");
                }
                if (username != null && !username.isEmpty()) {
                    request.setAttribute("username", username);
                    log.debug("Username added to request attributes: {}", username);
                }

                filterChain.doFilter(request, response);
                return;
            } else {
                log.warn("Invalid internal token received");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Invalid internal token\",\"message\":\"Request must come through Gateway\"}");
                return;
            }
        }

        // No internal token - BLOCK direct service calls (except public endpoints)
        log.warn("No internal token present - blocking direct service call to: {}", requestPath);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"Direct access forbidden\",\"message\":\"All requests must go through Gateway\"}");
        return;
    }

    public boolean isPublicEndpoint(String path) {
        return path.contains("/health") || 
               path.contains("/actuator") ||
               path.contains("/auth/register") ||
               path.contains("/auth/login") ||
                path.contains("/api/auth/register") ||
                path.contains("/api/auth/login");
    }

    private boolean validateInternalToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String source = claims.get("source", String.class);
            return "gateway".equals(source);
        } catch (Exception e) {
            log.debug("Internal token validation failed: {}", e.getMessage());
            return false;
        }
    }

    private String extractOriginalJwt(String internalToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(internalToken)
                    .getBody();
            
            return claims.get("originalJwt", String.class);
        } catch (Exception e) {
            log.error("Error extracting original JWT from internal token", e);
            return null;
        }
    }
}
