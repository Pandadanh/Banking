package com.panda.mdmService.controller;

import com.panda.mdmService.dto.CustomerTypeCreateRequest;
import com.panda.mdmService.dto.CustomerTypeDto;
import com.panda.mdmService.dto.CustomerTypeUpdateRequest;
import com.panda.mdmService.service.CustomerTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/customer-types")
@RequiredArgsConstructor
public class CustomerTypeController {
    private final CustomerTypeService customerTypeService;

    @GetMapping
    public Flux<CustomerTypeDto> getAll() {
        return customerTypeService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<CustomerTypeDto> getById(@PathVariable UUID id) {
        return customerTypeService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CustomerTypeDto> create(@RequestBody CustomerTypeCreateRequest req) {
        return customerTypeService.create(req);
    }

    @PutMapping("/{id}")
    public Mono<CustomerTypeDto> update(@PathVariable UUID id, @RequestBody CustomerTypeUpdateRequest req) {
        return customerTypeService.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable UUID id) {
        return customerTypeService.delete(id);
    }
} 