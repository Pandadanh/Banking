package com.panda.mdmService.controller;

import com.panda.mdmService.dto.EmployeeCreateRequest;
import com.panda.mdmService.dto.EmployeeDto;
import com.panda.mdmService.dto.EmployeeUpdateRequest;
import com.panda.mdmService.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping
    public Flux<EmployeeDto> getAll() {
        return employeeService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<EmployeeDto> getById(@PathVariable UUID id) {
        return employeeService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<EmployeeDto> create(@RequestBody EmployeeCreateRequest req, @RequestHeader("X-User-Id") UUID creatorId) {
        return employeeService.create(req, creatorId);
    }

    @PutMapping("/{id}")
    public Mono<EmployeeDto> update(@PathVariable UUID id, @RequestBody EmployeeUpdateRequest req, @RequestHeader("X-User-Id") UUID modifierId) {
        return employeeService.update(id, req, modifierId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable UUID id, @RequestHeader("X-User-Id") UUID deleterId) {
        return employeeService.delete(id, deleterId);
    }
} 