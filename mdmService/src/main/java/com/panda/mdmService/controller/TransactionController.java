package com.panda.mdmService.controller;

import com.panda.mdmService.dto.TransactionCreateRequest;
import com.panda.mdmService.dto.TransactionDto;
import com.panda.mdmService.dto.TransactionUpdateRequest;
import com.panda.mdmService.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping
    public Flux<TransactionDto> getAll() {
        return transactionService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<TransactionDto> getById(@PathVariable UUID id) {
        return transactionService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TransactionDto> create(@RequestBody TransactionCreateRequest req, @RequestHeader("X-User-Id") UUID creatorId) {
        return transactionService.create(req, creatorId);
    }

    @PutMapping("/{id}")
    public Mono<TransactionDto> update(@PathVariable UUID id, @RequestBody TransactionUpdateRequest req, @RequestHeader("X-User-Id") UUID modifierId) {
        return transactionService.update(id, req, modifierId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable UUID id, @RequestHeader("X-User-Id") UUID deleterId) {
        return transactionService.delete(id, deleterId);
    }
} 