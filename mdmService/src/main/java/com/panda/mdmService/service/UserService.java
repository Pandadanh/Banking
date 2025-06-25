package com.panda.mdmService.service;

import com.panda.mdmService.exception.InvalidUserDataException;
import com.panda.mdmService.exception.UserAlreadyExistsException;
import com.panda.mdmService.exception.UserNotFoundException;
import com.panda.mdmService.model.User;
import com.panda.mdmService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public Flux<User> getAllUsers() {
        log.debug("Getting all users");
        return userRepository.findAll()
                .doOnNext(user -> log.debug("Found user: {}", user.getId()))
                .doOnError(error -> log.error("Error getting all users", error));
    }

    public Mono<User> getUserById(String id) {
        log.debug("Getting user by id: {}", id);
        
        if (id == null || id.trim().isEmpty()) {
            return Mono.error(new InvalidUserDataException("User ID cannot be null or empty"));
        }

        return userRepository.findById(id)
                .doOnNext(user -> log.debug("Found user: {}", user.getId()))
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found with ID: " + id)))
                .doOnError(error -> log.error("Error getting user by id: {}", id, error));
    }

    public Mono<User> createUser(User user) {
        log.debug("Creating user: {}", user.getId());
        
        if (user == null) {
            return Mono.error(new InvalidUserDataException("User data cannot be null"));
        }

        return userRepository.existsById(String.valueOf(user.getId()))
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new UserAlreadyExistsException("User already exists with ID: " + user.getId()));
                    }
                    return userRepository.save(user);
                })
                .doOnNext(savedUser -> log.debug("Created user: {}", savedUser.getId()))
                .doOnError(error -> log.error("Error creating user: {}", user.getId(), error));
    }

    public Mono<User> updateUser(String id, User user) {
        log.debug("Updating user: {}", id);
        
        if (id == null || id.trim().isEmpty()) {
            return Mono.error(new InvalidUserDataException("User ID cannot be null or empty"));
        }

        if (user == null) {
            return Mono.error(new InvalidUserDataException("User data cannot be null"));
        }

        return userRepository.existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new UserNotFoundException("User not found with ID: " + id));
                    }
                    user.setId(UUID.fromString(id)); // Ensure the ID matches
                    return userRepository.save(user);
                })
                .doOnNext(updatedUser -> log.debug("Updated user: {}", updatedUser.getId()))
                .doOnError(error -> log.error("Error updating user: {}", id, error));
    }

    public Mono<Void> deleteUser(String id) {
        log.debug("Deleting user: {}", id);
        
        if (id == null || id.trim().isEmpty()) {
            return Mono.error(new InvalidUserDataException("User ID cannot be null or empty"));
        }

        return userRepository.existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new UserNotFoundException("User not found with ID: " + id));
                    }
                    return userRepository.deleteById(id);
                })
                .doOnSuccess(result -> log.debug("Deleted user: {}", id))
                .doOnError(error -> log.error("Error deleting user: {}", id, error));
    }
}
