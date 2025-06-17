package com.panda.mdmService.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;

@Component
@Slf4j
public class InternalTokenValidator {

    @Value("${security.jwt.internal-secret-key}")
    private String internalSecretKey;

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(internalSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateInternalToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("Internal token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public String extractUsername(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            log.error("Failed to extract username from internal token: {}", e.getMessage());
            return null;
        }
    }

    public String extractOriginalJwt(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
            return claims.get("originalJwt", String.class);
        } catch (Exception e) {
            log.error("Failed to extract original JWT from internal token: {}", e.getMessage());
            return null;
        }
    }
}
