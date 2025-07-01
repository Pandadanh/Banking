package com.panda.mdmService.service;

import com.panda.mdmService.dto.EmployeeTypeCreateRequest;
import com.panda.mdmService.dto.EmployeeTypeDto;
import com.panda.mdmService.dto.EmployeeTypeUpdateRequest;
import com.panda.mdmService.model.EmployeeType;
import com.panda.mdmService.repository.EmployeeTypeRepository;
import com.panda.mdmService.util.EmployeeTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeTypeService {
    private final EmployeeTypeRepository employeeTypeRepository;

    public Flux<EmployeeTypeDto> getAll() {
        return employeeTypeRepository.findAll().map(EmployeeTypeMapper::toDto);
    }

    public Mono<EmployeeTypeDto> getById(UUID id) {
        return employeeTypeRepository.findById(id).map(EmployeeTypeMapper::toDto);
    }

    public Mono<EmployeeTypeDto> create(EmployeeTypeCreateRequest req) {
        EmployeeType employeeType = EmployeeTypeMapper.fromCreateRequest(req);
        employeeType.setId(UUID.randomUUID());
        return employeeTypeRepository.save(employeeType).map(EmployeeTypeMapper::toDto);
    }

    public Mono<EmployeeTypeDto> update(UUID id, EmployeeTypeUpdateRequest req) {
        return employeeTypeRepository.findById(id)
                .flatMap(employeeType -> {
                    EmployeeTypeMapper.updateFromRequest(employeeType, req);
                    return employeeTypeRepository.save(employeeType);
                })
                .map(EmployeeTypeMapper::toDto);
    }

    public Mono<Void> delete(UUID id) {
        return employeeTypeRepository.deleteById(id);
    }
} 