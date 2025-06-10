package com.panda.mdmService.controller;

import com.panda.mdmService.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

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
    public Flux<User> getAllUsers() {
        return Flux.fromIterable(users.values());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> getUserById(@PathVariable String id) {
        return Mono.just(users.get(id))
                .map(user -> user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<User> createUser(@RequestBody User user) {
        users.put(user.getUserId(), user);
        return Mono.just(user);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<User>> updateUser(@PathVariable String id, @RequestBody User user) {
        return Mono.just(users.containsKey(id))
                .map(exists -> {
                    if (exists) {
                        users.put(id, user);
                        return ResponseEntity.ok(user);
                    }
                    return ResponseEntity.notFound().build();
                });
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable String id) {
        return Mono.just(users.containsKey(id))
                .map(exists -> {
                    if (exists) {
                        users.remove(id);
                        return ResponseEntity.ok().<Void>build();
                    }
                    return ResponseEntity.notFound().build();
                });
    }
} 