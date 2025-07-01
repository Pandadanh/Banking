package com.panda.mdmService.controller;

import com.panda.mdmService.dto.DepartmentCreateRequest;
import com.panda.mdmService.dto.DepartmentDto;
import com.panda.mdmService.dto.DepartmentUpdateRequest;
import com.panda.mdmService.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping
    public Flux<DepartmentDto> getAll() {
        return departmentService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<DepartmentDto> getById(@PathVariable UUID id) {
        return departmentService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<DepartmentDto> create(@RequestBody DepartmentCreateRequest req) {
        return departmentService.create(req);
    }

    @PutMapping("/{id}")
    public Mono<DepartmentDto> update(@PathVariable UUID id, @RequestBody DepartmentUpdateRequest req) {
        return departmentService.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable UUID id) {
        return departmentService.delete(id);
    }
} 