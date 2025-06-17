package com.panda.mdmService.security;

public interface JwtTokenProvider {
    boolean validateToken(String token);
    String getUsernameFromToken(String token);
}
