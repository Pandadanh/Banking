package com.panda.mdmService.service;

import com.panda.mdmService.dto.EmployeeCreateRequest;
import com.panda.mdmService.dto.EmployeeDto;
import com.panda.mdmService.dto.EmployeeUpdateRequest;
import com.panda.mdmService.model.Employee;
import com.panda.mdmService.repository.EmployeeRepository;
import com.panda.mdmService.util.EmployeeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public Flux<EmployeeDto> getAll() {
        return employeeRepository.findAll().map(EmployeeMapper::toDto);
    }

    public Mono<EmployeeDto> getById(UUID id) {
        return employeeRepository.findById(id).map(EmployeeMapper::toDto);
    }

    public Mono<EmployeeDto> create(EmployeeCreateRequest req, UUID creatorId) {
        Employee employee = EmployeeMapper.fromCreateRequest(req);
        employee.setId(UUID.randomUUID());
        employee.setCreationTime(LocalDateTime.now());
        employee.setCreatorId(creatorId);
        return employeeRepository.save(employee).map(EmployeeMapper::toDto);
    }

    public Mono<EmployeeDto> update(UUID id, EmployeeUpdateRequest req, UUID modifierId) {
        return employeeRepository.findById(id)
                .flatMap(employee -> {
                    EmployeeMapper.updateFromRequest(employee, req);
                    employee.setLastModificationTime(LocalDateTime.now());
                    employee.setLastModifierId(modifierId);
                    return employeeRepository.save(employee);
                })
                .map(EmployeeMapper::toDto);
    }

    public Mono<Void> delete(UUID id, UUID deleterId) {
        return employeeRepository.findById(id)
                .flatMap(employee -> {
                    employee.setIsDeleted(true);
                    employee.setDeletionTime(LocalDateTime.now());
                    employee.setDeleterId(deleterId);
                    return employeeRepository.save(employee);
                })
                .then();
    }
} 