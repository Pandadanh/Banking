package com.panda.authenService.service;

import com.panda.authenService.dto.LoginRequest;
import com.panda.authenService.dto.RegisterRequest;
import com.panda.authenService.dto.TokenResponse;
import com.panda.authenService.exception.AuthenticationFailedException;
import com.panda.authenService.exception.InvalidTokenException;
import com.panda.authenService.exception.UserAlreadyExistsException;
import com.panda.authenService.security.SimpleJwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final SimpleJwtTokenProvider tokenProvider;

    public TokenResponse register(RegisterRequest request) {
        // Simple mock implementation for testing
        // In real implementation, check if user already exists
        if ("existinguser".equals(request.getUsername())) {
            throw new UserAlreadyExistsException("Username '" + request.getUsername() + "' is already taken");
        }

        String mockToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTcxODYwNDAwMCwiZXhwIjoxNzE4NjA3NjAwfQ.mock";
        return new TokenResponse(mockToken);
    }

    public TokenResponse login(LoginRequest request) {
        // Simple mock implementation for testing
        // In real implementation, validate credentials against database
        if ("testuser".equals(request.getUsername()) && "password".equals(request.getPassword())) {
            String mockToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTcxODYwNDAwMCwiZXhwIjoxNzE4NjA3NjAwfQ.mock";
            return new TokenResponse(mockToken);
        }
        throw new AuthenticationFailedException("Invalid username or password");
    }

    public boolean validateToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new InvalidTokenException("Token cannot be null or empty");
        }

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            return tokenProvider.validateToken(token);
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid token format or signature");
        }
    }
} 