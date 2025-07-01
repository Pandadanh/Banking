package com.panda.mdmService.controller;

import com.panda.mdmService.dto.StoreRouteCreateRequest;
import com.panda.mdmService.dto.StoreRouteDto;
import com.panda.mdmService.dto.StoreRouteUpdateRequest;
import com.panda.mdmService.service.StoreRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/store-routes")
@RequiredArgsConstructor
public class StoreRouteController {
    private final StoreRouteService storeRouteService;

    @GetMapping
    public Flux<StoreRouteDto> getAll() {
        return storeRouteService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<StoreRouteDto> getById(@PathVariable UUID id) {
        return storeRouteService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<StoreRouteDto> create(@RequestBody StoreRouteCreateRequest req, @RequestHeader("X-User-Id") UUID creatorId) {
        return storeRouteService.create(req, creatorId);
    }

    @PutMapping("/{id}")
    public Mono<StoreRouteDto> update(@PathVariable UUID id, @RequestBody StoreRouteUpdateRequest req, @RequestHeader("X-User-Id") UUID modifierId) {
        return storeRouteService.update(id, req, modifierId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable UUID id, @RequestHeader("X-User-Id") UUID deleterId) {
        return storeRouteService.delete(id, deleterId);
    }
} 