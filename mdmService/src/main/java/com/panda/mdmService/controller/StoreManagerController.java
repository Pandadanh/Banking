package com.panda.mdmService.controller;

import com.panda.mdmService.dto.StoreManagerCreateRequest;
import com.panda.mdmService.dto.StoreManagerDto;
import com.panda.mdmService.dto.StoreManagerUpdateRequest;
import com.panda.mdmService.service.StoreManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/store-managers")
@RequiredArgsConstructor
public class StoreManagerController {
    private final StoreManagerService storeManagerService;

    @GetMapping
    public Flux<StoreManagerDto> getAll() {
        return storeManagerService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<StoreManagerDto> getById(@PathVariable UUID id) {
        return storeManagerService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<StoreManagerDto> create(@RequestBody StoreManagerCreateRequest req, @RequestHeader("X-User-Id") UUID creatorId) {
        return storeManagerService.create(req, creatorId);
    }

    @PutMapping("/{id}")
    public Mono<StoreManagerDto> update(@PathVariable UUID id, @RequestBody StoreManagerUpdateRequest req, @RequestHeader("X-User-Id") UUID modifierId) {
        return storeManagerService.update(id, req, modifierId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable UUID id, @RequestHeader("X-User-Id") UUID deleterId) {
        return storeManagerService.delete(id, deleterId);
    }
} 