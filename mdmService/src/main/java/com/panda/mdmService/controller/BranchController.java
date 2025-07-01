package com.panda.mdmService.controller;

import com.panda.mdmService.dto.BranchCreateRequest;
import com.panda.mdmService.dto.BranchDto;
import com.panda.mdmService.dto.BranchUpdateRequest;
import com.panda.mdmService.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class BranchController {
    private final BranchService branchService;

    @GetMapping
    public Flux<BranchDto> getAll() {
        return branchService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<BranchDto> getById(@PathVariable UUID id) {
        return branchService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BranchDto> create(@RequestBody BranchCreateRequest req, @RequestHeader("X-User-Id") UUID creatorId) {
        return branchService.create(req, creatorId);
    }

    @PutMapping("/{id}")
    public Mono<BranchDto> update(@PathVariable UUID id, @RequestBody BranchUpdateRequest req, @RequestHeader("X-User-Id") UUID modifierId) {
        return branchService.update(id, req, modifierId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable UUID id, @RequestHeader("X-User-Id") UUID deleterId) {
        return branchService.delete(id, deleterId);
    }
} 