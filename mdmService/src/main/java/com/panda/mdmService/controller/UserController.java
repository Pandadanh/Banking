package com.panda.mdmService.controller;

import com.panda.mdmService.exception.InvalidUserDataException;
import com.panda.mdmService.exception.UserAlreadyExistsException;
import com.panda.mdmService.exception.UserNotFoundException;
import com.panda.mdmService.model.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<String, User> users = new ConcurrentHashMap<>();

    public UserController() {
        // Thêm dữ liệu mẫu
        User user = new User(
            "7d7584f9-9dc3-e299-2c66-3a1a4723f1ba",
            "0773102102",
            "b93957cf-2faf-654b-0a9d-3a18a5c1c21b",
            "OFVMT",
            "6ed8d90f-df9a-4400-6775-08dda27aaa3e",
            "EMPPG0013",
            "Employee 13",
            "",
            "PG"
        );
        users.put(user.getUserId(), user);
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidUserDataException("User ID cannot be null or empty");
        }

        User user = users.get(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        if (user == null) {
            throw new InvalidUserDataException("User data cannot be null");
        }

        if (user.getUserId() == null || user.getUserId().trim().isEmpty()) {
            throw new InvalidUserDataException("User ID cannot be null or empty");
        }

        if (users.containsKey(user.getUserId())) {
            throw new UserAlreadyExistsException(user.getUserId());
        }

        users.put(user.getUserId(), user);
        return user;
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @Valid @RequestBody User user) {
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidUserDataException("User ID cannot be null or empty");
        }

        if (user == null) {
            throw new InvalidUserDataException("User data cannot be null");
        }

        if (!users.containsKey(id)) {
            throw new UserNotFoundException(id);
        }

        users.put(id, user);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidUserDataException("User ID cannot be null or empty");
        }

        if (!users.containsKey(id)) {
            throw new UserNotFoundException(id);
        }

        users.remove(id);
        return ResponseEntity.ok().build();
    }
} 