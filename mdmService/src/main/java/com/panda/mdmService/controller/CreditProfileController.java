package com.panda.mdmService.controller;

import com.panda.mdmService.dto.CreditProfileCreateRequest;
import com.panda.mdmService.dto.CreditProfileDto;
import com.panda.mdmService.dto.CreditProfileUpdateRequest;
import com.panda.mdmService.service.CreditProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/credit-profiles")
@RequiredArgsConstructor
public class CreditProfileController {
    private final CreditProfileService creditProfileService;

    @GetMapping
    public Flux<CreditProfileDto> getAll() {
        return creditProfileService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<CreditProfileDto> getById(@PathVariable UUID id) {
        return creditProfileService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CreditProfileDto> create(@RequestBody CreditProfileCreateRequest req) {
        return creditProfileService.create(req);
    }

    @PutMapping("/{id}")
    public Mono<CreditProfileDto> update(@PathVariable UUID id, @RequestBody CreditProfileUpdateRequest req) {
        return creditProfileService.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable UUID id) {
        return creditProfileService.delete(id);
    }
} 