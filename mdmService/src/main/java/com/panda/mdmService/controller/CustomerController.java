package com.panda.mdmService.controller;

import com.panda.mdmService.dto.CustomerCreateRequest;
import com.panda.mdmService.dto.CustomerDto;
import com.panda.mdmService.dto.CustomerUpdateRequest;
import com.panda.mdmService.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    public Flux<CustomerDto> getAll() {
        return customerService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<CustomerDto> getById(@PathVariable UUID id) {
        return customerService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CustomerDto> create(@RequestBody CustomerCreateRequest req, @RequestHeader("X-User-Id") UUID creatorId) {
        return customerService.create(req, creatorId);
    }

    @PutMapping("/{id}")
    public Mono<CustomerDto> update(@PathVariable UUID id, @RequestBody CustomerUpdateRequest req, @RequestHeader("X-User-Id") UUID modifierId) {
        return customerService.update(id, req, modifierId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable UUID id, @RequestHeader("X-User-Id") UUID deleterId) {
        return customerService.delete(id, deleterId);
    }
} 