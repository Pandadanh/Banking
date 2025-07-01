package com.panda.mdmService.controller;

import com.panda.mdmService.dto.NotificationCreateRequest;
import com.panda.mdmService.dto.NotificationDto;
import com.panda.mdmService.dto.NotificationUpdateRequest;
import com.panda.mdmService.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public Flux<NotificationDto> getAll() {
        return notificationService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<NotificationDto> getById(@PathVariable UUID id) {
        return notificationService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<NotificationDto> create(@RequestBody NotificationCreateRequest req) {
        return notificationService.create(req);
    }

    @PutMapping("/{id}")
    public Mono<NotificationDto> update(@PathVariable UUID id, @RequestBody NotificationUpdateRequest req) {
        return notificationService.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable UUID id) {
        return notificationService.delete(id);
    }
} 