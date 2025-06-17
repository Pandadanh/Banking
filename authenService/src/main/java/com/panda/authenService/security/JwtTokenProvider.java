package com.panda.authenService.security;

public interface JwtTokenProvider {
    boolean validateToken(String token);
    String getUsernameFromToken(String token);
}
