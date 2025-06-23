package com.panda.authenService.service;

import com.panda.authenService.dto.LoginRequest;
import com.panda.authenService.dto.RegisterRequest;
import com.panda.authenService.dto.TokenResponse;
import com.panda.authenService.entity.Employee;
import com.panda.authenService.entity.HistoryLoginTenant;
import com.panda.authenService.entity.LoginTransaction;
import com.panda.authenService.entity.User;
import com.panda.authenService.exception.AuthenticationFailedException;
import com.panda.authenService.exception.InvalidTokenException;
import com.panda.authenService.exception.UserAlreadyExistsException;
import com.panda.authenService.repository.EmployeeRepository;
import com.panda.authenService.repository.HistoryLoginTenantRepository;
import com.panda.authenService.repository.LoginTransactionRepository;
import com.panda.authenService.repository.UserRepository;
import com.panda.authenService.security.SimpleJwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final SimpleJwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final HistoryLoginTenantRepository historyLoginTenantRepository;
    private final LoginTransactionRepository loginTransactionRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenCacheService tokenCacheService;

    @Transactional
    public TokenResponse register(RegisterRequest request) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        log.info("Registering new user: {}", request.getUsername());

        // Check if user already exists
        if (userRepository.existsByUserNameAndIsDeletedFalse(request.getUsername())) {
            throw new UserAlreadyExistsException("Username '" + request.getUsername() + "' is already taken");
        }

        if (userRepository.existsByEmailAndIsDeletedFalse(request.getEmail())) {
            throw new UserAlreadyExistsException("Email '" + request.getEmail() + "' is already in use");
        }

        // Create new user
        User user = new User();
        user.setUserName(request.getUsername());
        user.setNormalizedUserName(request.getUsername().toUpperCase());
        user.setEmail(request.getEmail());
        user.setNormalizedEmail(request.getEmail().toUpperCase());
        user.setName(request.getFullName());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setEmailConfirmed(true);
        user.setIsActive(true);

        user = userRepository.save(user);
        log.info("User registered successfully with ID: {}", user.getId());

        // Generate token
        String accessToken = tokenCacheService.generateToken(user);

        // Log successful registration
        logLoginTransaction(user, null, "SUC", null);

        return new TokenResponse(accessToken);
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        log.info("Login attempt for user: {}", request.getUsername());

        try {
            // Find user by username
            Optional<User> userOpt = userRepository.findActiveUserByUserName(request.getUsername());
            if (userOpt.isEmpty()) {
                logLoginTransaction(null, request.getUsername(), "FAI", "User not found");
                throw new AuthenticationFailedException("Invalid username or password");
            }

            User user = userOpt.get();

            // Check password
            if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
                logLoginTransaction(user, request.getUsername(), "FAI", "Invalid password");
                throw new AuthenticationFailedException("Invalid username or password");
            }

            // Check if user is locked out
            if (isUserLockedOut(user)) {
                logLoginTransaction(user, request.getUsername(), "FAI", "User is locked out");
                throw new AuthenticationFailedException("Account is locked due to multiple failed attempts");
            }

            // Check for existing valid token
            String existingToken = tokenCacheService.getExistingValidToken(user);
            if (existingToken != null) {
                log.info("Returning existing valid token for user: {}", user.getUserName());
                logLoginTransaction(user, request.getUsername(), "SUC", "Existing token returned");
                return new TokenResponse(existingToken);
            }

            // Generate new token
            String accessToken = tokenCacheService.generateToken(user);

            // Reset failed attempts on successful login
            user.setAccessFailedCount(0);
            userRepository.save(user);

            // Log successful login
            logLoginTransaction(user, request.getUsername(), "SUC", null);

            log.info("Login successful for user: {}", user.getUserName());
            return new TokenResponse(accessToken);

        } catch (AuthenticationFailedException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during login for user: {}", request.getUsername(), e);
            logLoginTransaction(null, request.getUsername(), "FAI", "System error: " + e.getMessage());
            throw new AuthenticationFailedException("Login failed due to system error");
        }
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

    private boolean isUserLockedOut(User user) {
        if (!user.getLockoutEnabled()) {
            return false;
        }

        if (user.getLockoutEnd() != null && user.getLockoutEnd().isAfter(java.time.OffsetDateTime.now())) {
            return true;
        }

        // Check failed attempts in last 15 minutes
        LocalDateTime fifteenMinutesAgo = LocalDateTime.now().minusMinutes(15);
        long failedAttempts = loginTransactionRepository.countFailedLoginAttempts(user.getUserName(), fifteenMinutesAgo);

        if (failedAttempts >= 5) {
            // Lock user for 30 minutes
            user.setLockoutEnd(java.time.OffsetDateTime.now().plusMinutes(30));
            user.setLockoutEnabled(true);
            userRepository.save(user);
            return true;
        }

        return false;
    }

    private void logLoginTransaction(User user, String userName, String status, String error) {
        try {
            LoginTransaction transaction = new LoginTransaction();
            transaction.setUserId(user != null ? user.getId() : null);
            transaction.setTenantId(user != null ? user.getTenantId() : null);
            transaction.setUserName(userName);
            transaction.setLoginStatus(status);
            transaction.setError(error);

            // Try to get employee info
            if (user != null) {
                Optional<Employee> employeeOpt = employeeRepository.findByUserIdAndIsDeletedFalse(user.getId());
                if (employeeOpt.isPresent()) {
                    transaction.setEmployeeId(employeeOpt.get().getId());
                    transaction.setTenantCode(employeeOpt.get().getCode());
                }
            }

            loginTransactionRepository.save(transaction);
        } catch (Exception e) {
            log.error("Failed to log login transaction", e);
        }
    }
} 