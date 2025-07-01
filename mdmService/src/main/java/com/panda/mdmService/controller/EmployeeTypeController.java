package com.panda.mdmService.controller;

import com.panda.mdmService.dto.EmployeeTypeCreateRequest;
import com.panda.mdmService.dto.EmployeeTypeDto;
import com.panda.mdmService.dto.EmployeeTypeUpdateRequest;
import com.panda.mdmService.service.EmployeeTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/employee-types")
@RequiredArgsConstructor
public class EmployeeTypeController {
    private final EmployeeTypeService employeeTypeService;

    @GetMapping
    public Flux<EmployeeTypeDto> getAll() {
        return employeeTypeService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<EmployeeTypeDto> getById(@PathVariable UUID id) {
        return employeeTypeService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<EmployeeTypeDto> create(@RequestBody EmployeeTypeCreateRequest req) {
        return employeeTypeService.create(req);
    }

    @PutMapping("/{id}")
    public Mono<EmployeeTypeDto> update(@PathVariable UUID id, @RequestBody EmployeeTypeUpdateRequest req) {
        return employeeTypeService.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable UUID id) {
        return employeeTypeService.delete(id);
    }
} 