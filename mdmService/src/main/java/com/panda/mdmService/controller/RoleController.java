package com.panda.mdmService.controller;

import com.panda.mdmService.dto.RoleCreateRequest;
import com.panda.mdmService.dto.RoleDto;
import com.panda.mdmService.dto.RoleUpdateRequest;
import com.panda.mdmService.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    public Flux<RoleDto> getAll() {
        return roleService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<RoleDto> getById(@PathVariable UUID id) {
        return roleService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<RoleDto> create(@RequestBody RoleCreateRequest req) {
        return roleService.create(req);
    }

    @PutMapping("/{id}")
    public Mono<RoleDto> update(@PathVariable UUID id, @RequestBody RoleUpdateRequest req) {
        return roleService.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable UUID id) {
        return roleService.delete(id);
    }
} 