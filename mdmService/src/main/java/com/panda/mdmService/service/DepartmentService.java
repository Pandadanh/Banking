package com.panda.mdmService.service;

import com.panda.mdmService.dto.DepartmentCreateRequest;
import com.panda.mdmService.dto.DepartmentDto;
import com.panda.mdmService.dto.DepartmentUpdateRequest;
import com.panda.mdmService.model.Department;
import com.panda.mdmService.repository.DepartmentRepository;
import com.panda.mdmService.util.DepartmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public Flux<DepartmentDto> getAll() {
        return departmentRepository.findAll().map(DepartmentMapper::toDto);
    }

    public Mono<DepartmentDto> getById(UUID id) {
        return departmentRepository.findById(id).map(DepartmentMapper::toDto);
    }

    public Mono<DepartmentDto> create(DepartmentCreateRequest req) {
        Department department = DepartmentMapper.fromCreateRequest(req);
        department.setId(UUID.randomUUID());
        return departmentRepository.save(department).map(DepartmentMapper::toDto);
    }

    public Mono<DepartmentDto> update(UUID id, DepartmentUpdateRequest req) {
        return departmentRepository.findById(id)
                .flatMap(department -> {
                    DepartmentMapper.updateFromRequest(department, req);
                    return departmentRepository.save(department);
                })
                .map(DepartmentMapper::toDto);
    }

    public Mono<Void> delete(UUID id) {
        return departmentRepository.deleteById(id);
    }
} 