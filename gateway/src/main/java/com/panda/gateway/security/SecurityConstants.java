package com.panda.gateway.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecurityConstants {
    public static String PUBLIC_KEY;
    public static String AUTHORIZATION_HEADER;
    public static String TOKEN_PREFIX;

    @Value("${security.jwt.public-key}")
    public void setPublicKey(String publicKey) {
        PUBLIC_KEY = publicKey;
    }

    @Value("${security.jwt.authorization-header}")
    public void setAuthorizationHeader(String authorizationHeader) {
        AUTHORIZATION_HEADER = authorizationHeader;
    }

    @Value("${security.jwt.token-prefix}")
    public void setTokenPrefix(String tokenPrefix) {
        TOKEN_PREFIX = tokenPrefix;
    }
} 