package com.panda.mdmService.service;

import com.panda.mdmService.dto.CustomerTypeCreateRequest;
import com.panda.mdmService.dto.CustomerTypeDto;
import com.panda.mdmService.dto.CustomerTypeUpdateRequest;
import com.panda.mdmService.model.CustomerType;
import com.panda.mdmService.repository.CustomerTypeRepository;
import com.panda.mdmService.util.CustomerTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerTypeService {
    private final CustomerTypeRepository customerTypeRepository;

    public Flux<CustomerTypeDto> getAll() {
        return customerTypeRepository.findAll().map(CustomerTypeMapper::toDto);
    }

    public Mono<CustomerTypeDto> getById(UUID id) {
        return customerTypeRepository.findById(id).map(CustomerTypeMapper::toDto);
    }

    public Mono<CustomerTypeDto> create(CustomerTypeCreateRequest req) {
        CustomerType customerType = CustomerTypeMapper.fromCreateRequest(req);
        customerType.setId(UUID.randomUUID());
        return customerTypeRepository.save(customerType).map(CustomerTypeMapper::toDto);
    }

    public Mono<CustomerTypeDto> update(UUID id, CustomerTypeUpdateRequest req) {
        return customerTypeRepository.findById(id)
                .flatMap(customerType -> {
                    CustomerTypeMapper.updateFromRequest(customerType, req);
                    return customerTypeRepository.save(customerType);
                })
                .map(CustomerTypeMapper::toDto);
    }

    public Mono<Void> delete(UUID id) {
        return customerTypeRepository.deleteById(id);
    }
} 