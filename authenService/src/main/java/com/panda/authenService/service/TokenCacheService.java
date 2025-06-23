package com.panda.authenService.service;

import com.panda.authenService.entity.HistoryLoginTenant;
import com.panda.authenService.entity.User;
import com.panda.authenService.repository.HistoryLoginTenantRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenCacheService {

    private final HistoryLoginTenantRepository historyLoginTenantRepository;

    @Value("${security.jwt.private-key}")
    private String privateKey;

    @Value("${security.jwt.expiration:86400000}") // 24 hours default
    private long jwtExpiration;

    private PrivateKey getSigningKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        try {
            String privateKeyPem = this.privateKey;
            if (privateKeyPem == null || privateKeyPem.isEmpty()) {
                throw new IllegalArgumentException("Private key is empty or null");
            }

            // Thêm header và footer nếu chưa có
            if (!privateKeyPem.contains("-----BEGIN PRIVATE KEY-----")) {
                privateKeyPem = "-----BEGIN PRIVATE KEY-----\n" + privateKeyPem + "\n-----END PRIVATE KEY-----";
            }

            // Xóa header, footer và khoảng trắng
            privateKeyPem = privateKeyPem
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");

            // Decode Base64 và tạo private key
            byte[] keyBytes = Base64.getDecoder().decode(privateKeyPem);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            try {
                return keyFactory.generatePrivate(keySpec);
            } catch (InvalidKeySpecException e) {
                log.error("Invalid key format: {}", e.getMessage());
                throw new InvalidKeySpecException("Invalid private key format. Please ensure the key is in PKCS#8 format.");
            }
        } catch (IllegalArgumentException e) {
            log.error("Base64 decoding failed: {}", e.getMessage());
            throw new InvalidKeySpecException("Failed to decode private key. Invalid Base64 format.");
        } catch (Exception e) {
            log.error("Failed to load private key: {}", e.getMessage());
            throw e;
        }
    }

    @Transactional
    public String generateToken(User user) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        log.info("Generating token for user: {}", user.getUserName());

        // Check for existing valid token
        String existingToken = getExistingValidToken(user);
        if (existingToken != null) {
            log.info("Returning existing valid token for user: {}", user.getUserName());
            return existingToken;
        }

        // Generate new token
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        String accessToken = Jwts.builder()
                .setSubject(user.getUserName())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("userId", user.getId().toString())
                .claim("tenantId", user.getTenantId() != null ? user.getTenantId().toString() : null)
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .signWith(getSigningKey(), SignatureAlgorithm.RS256)
                .compact();

        // Generate refresh token (longer expiry)
        Date refreshExpiryDate = new Date(now.getTime() + (jwtExpiration * 7)); // 7 days
        String refreshToken = Jwts.builder()
                .setSubject(user.getUserName())
                .setIssuedAt(now)
                .setExpiration(refreshExpiryDate)
                .claim("type", "refresh")
                .claim("userId", user.getId().toString())
                .signWith(getSigningKey(), SignatureAlgorithm.RS256)
                .compact();

        // Save to history
        saveTokenHistory(user, accessToken, refreshToken);

        log.info("New token generated for user: {}", user.getUserName());
        return accessToken;
    }

    public String getExistingValidToken(User user) {
        try {
            Optional<HistoryLoginTenant> historyOpt = historyLoginTenantRepository
                    .findLatestValidTokenByUserAndTenant(user.getId(), user.getTenantId());

            if (historyOpt.isPresent()) {
                HistoryLoginTenant history = historyOpt.get();
                String token = history.getAccessToken();

                if (isTokenValid(token)) {
                    log.info("Found existing valid token for user: {}", user.getUserName());
                    return token;
                } else {
                    // Token expired, mark as deleted
                    history.setIsDeleted(true);
                    historyLoginTenantRepository.save(history);
                    log.info("Existing token expired for user: {}", user.getUserName());
                }
            }
        } catch (Exception e) {
            log.error("Error checking existing token for user: {}", user.getUserName(), e);
        }

        return null;
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Date expiration = claims.getExpiration();
            return expiration.after(new Date());
        } catch (Exception e) {
            log.debug("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public String refreshToken(String refreshToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();

            String userName = claims.getSubject();
            String userIdStr = claims.get("userId", String.class);
            UUID userId = UUID.fromString(userIdStr);

            // Find the refresh token in history
            Optional<HistoryLoginTenant> historyOpt = historyLoginTenantRepository
                    .findByRefreshToken(refreshToken);

            if (historyOpt.isPresent() && !historyOpt.get().getIsDeleted()) {
                HistoryLoginTenant history = historyOpt.get();

                // Generate new access token
                Date now = new Date();
                Date expiryDate = new Date(now.getTime() + jwtExpiration);

                String newAccessToken = Jwts.builder()
                        .setSubject(userName)
                        .setIssuedAt(now)
                        .setExpiration(expiryDate)
                        .claim("userId", userIdStr)
                        .claim("tenantId", claims.get("tenantId"))
                        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                        .compact();

                // Update history with new access token
                history.setAccessToken(newAccessToken);
                history.setLastModificationTime(LocalDateTime.now());
                historyLoginTenantRepository.save(history);

                log.info("Token refreshed for user: {}", userName);
                return newAccessToken;
            }
        } catch (Exception e) {
            log.error("Error refreshing token", e);
        }

        return null;
    }

    @Transactional
    public void invalidateToken(String token) {
        try {
            Optional<HistoryLoginTenant> historyOpt = historyLoginTenantRepository
                    .findByAccessToken(token);

            if (historyOpt.isPresent()) {
                HistoryLoginTenant history = historyOpt.get();
                history.setIsDeleted(true);
                history.setDeletionTime(LocalDateTime.now());
                historyLoginTenantRepository.save(history);
                log.info("Token invalidated for user: {}", history.getUserName());
            }
        } catch (Exception e) {
            log.error("Error invalidating token", e);
        }
    }

    @Transactional
    public void invalidateAllUserTokens(UUID userId) {
        try {
            historyLoginTenantRepository.findByGlobalUserIdAndIsDeletedFalse(userId)
                    .forEach(history -> {
                        history.setIsDeleted(true);
                        history.setDeletionTime(LocalDateTime.now());
                        historyLoginTenantRepository.save(history);
                    });
            log.info("All tokens invalidated for user ID: {}", userId);
        } catch (Exception e) {
            log.error("Error invalidating all tokens for user: {}", userId, e);
        }
    }

    private void saveTokenHistory(User user, String accessToken, String refreshToken) {
        try {
            HistoryLoginTenant history = new HistoryLoginTenant();
            history.setGlobalUserId(user.getId());
            history.setTenantId(user.getTenantId());
            history.setUserName(user.getUserName());
            history.setAccessToken(accessToken);
            history.setRefreshToken(refreshToken);
            history.setCreationTime(LocalDateTime.now());

            historyLoginTenantRepository.save(history);
            log.debug("Token history saved for user: {}", user.getUserName());
        } catch (Exception e) {
            log.error("Error saving token history for user: {}", user.getUserName(), e);
        }
    }
}
