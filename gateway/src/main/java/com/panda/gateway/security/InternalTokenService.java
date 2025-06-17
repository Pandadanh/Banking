package com.panda.gateway.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Service
@Slf4j
public class InternalTokenService {

    @Value("${security.jwt.internal-secret-key}")
    private String internalSecretKey;

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(internalSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateInternalToken(String username, String originalJwt) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 300000); // 5 minutes

        return Jwts.builder()
                .setSubject(username)
                .claim("originalJwt", originalJwt)
                .claim("source", "gateway")
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
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
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String extractOriginalJwt(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("originalJwt", String.class);
    }
}
