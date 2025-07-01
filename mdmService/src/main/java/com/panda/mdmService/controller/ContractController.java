package com.panda.mdmService.controller;

import com.panda.mdmService.dto.ContractCreateRequest;
import com.panda.mdmService.dto.ContractDto;
import com.panda.mdmService.dto.ContractUpdateRequest;
import com.panda.mdmService.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
public class ContractController {
    private final ContractService contractService;

    @GetMapping
    public Flux<ContractDto> getAll() {
        return contractService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<ContractDto> getById(@PathVariable UUID id) {
        return contractService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ContractDto> create(@RequestBody ContractCreateRequest req, @RequestHeader("X-User-Id") UUID creatorId) {
        return contractService.create(req, creatorId);
    }

    @PutMapping("/{id}")
    public Mono<ContractDto> update(@PathVariable UUID id, @RequestBody ContractUpdateRequest req, @RequestHeader("X-User-Id") UUID modifierId) {
        return contractService.update(id, req, modifierId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable UUID id, @RequestHeader("X-User-Id") UUID deleterId) {
        return contractService.delete(id, deleterId);
    }
} 