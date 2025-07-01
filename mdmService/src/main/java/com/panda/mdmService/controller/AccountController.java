package com.panda.mdmService.controller;

import com.panda.mdmService.dto.AccountCreateRequest;
import com.panda.mdmService.dto.AccountDto;
import com.panda.mdmService.dto.AccountUpdateRequest;
import com.panda.mdmService.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    public Flux<AccountDto> getAll() {
        return accountService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<AccountDto> getById(@PathVariable UUID id) {
        return accountService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AccountDto> create(@RequestBody AccountCreateRequest req, @RequestHeader("X-User-Id") UUID creatorId) {
        return accountService.create(req, creatorId);
    }

    @PutMapping("/{id}")
    public Mono<AccountDto> update(@PathVariable UUID id, @RequestBody AccountUpdateRequest req, @RequestHeader("X-User-Id") UUID modifierId) {
        return accountService.update(id, req, modifierId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable UUID id, @RequestHeader("X-User-Id") UUID deleterId) {
        return accountService.delete(id, deleterId);
    }
} 