package com.panda.gateway.service;

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

    @Value("${security.jwt.secret}")
    private String jwtSecret;

    @Value("${security.jwt.internal-expiration:300000}") // 5 minutes default
    private long internalTokenExpiration;

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generate internal token for service-to-service communication
     */
    public String generateInternalToken(String service, String originalJwt) {
        try {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + internalTokenExpiration);

            String internalToken = Jwts.builder()
                    .setSubject("INTERNAL")
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .claim("service", service)
                    .claim("originalJwt", originalJwt)
                    .claim("type", "internal")
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();

            log.debug("Generated internal token for service: {}", service);
            return internalToken;

        } catch (Exception e) {
            log.error("Error generating internal token for service: {}", service, e);
            throw new RuntimeException("Failed to generate internal token", e);
        }
    }

    /**
     * Validate internal token
     */
    public boolean validateInternalToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);

            log.debug("Internal token validation successful");
            return true;
        } catch (Exception e) {
            log.debug("Internal token validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Extract original JWT from internal token
     */
    public String extractOriginalJwt(String internalToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(internalToken)
                    .getBody()
                    .get("originalJwt", String.class);
        } catch (Exception e) {
            log.error("Error extracting original JWT from internal token", e);
            return null;
        }
    }

    /**
     * Extract service name from internal token
     */
    public String extractService(String internalToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(internalToken)
                    .getBody()
                    .get("service", String.class);
        } catch (Exception e) {
            log.error("Error extracting service from internal token", e);
            return null;
        }
    }
}
