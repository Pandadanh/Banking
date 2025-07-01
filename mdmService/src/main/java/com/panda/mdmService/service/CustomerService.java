package com.panda.mdmService.service;

import com.panda.mdmService.dto.CustomerCreateRequest;
import com.panda.mdmService.dto.CustomerDto;
import com.panda.mdmService.dto.CustomerUpdateRequest;
import com.panda.mdmService.model.Customer;
import com.panda.mdmService.repository.CustomerRepository;
import com.panda.mdmService.util.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Flux<CustomerDto> getAll() {
        return customerRepository.findAll().map(CustomerMapper::toDto);
    }

    public Mono<CustomerDto> getById(UUID id) {
        return customerRepository.findById(id).map(CustomerMapper::toDto);
    }

    public Mono<CustomerDto> create(CustomerCreateRequest req, UUID creatorId) {
        Customer customer = CustomerMapper.fromCreateRequest(req);
        customer.setId(UUID.randomUUID());
        customer.setCreationTime(LocalDateTime.now());
        customer.setCreatorId(creatorId);
        return customerRepository.save(customer).map(CustomerMapper::toDto);
    }

    public Mono<CustomerDto> update(UUID id, CustomerUpdateRequest req, UUID modifierId) {
        return customerRepository.findById(id)
                .flatMap(customer -> {
                    CustomerMapper.updateFromRequest(customer, req);
                    customer.setLastModificationTime(LocalDateTime.now());
                    customer.setLastModifierId(modifierId);
                    return customerRepository.save(customer);
                })
                .map(CustomerMapper::toDto);
    }

    public Mono<Void> delete(UUID id, UUID deleterId) {
        return customerRepository.findById(id)
                .flatMap(customer -> {
                    customer.setIsDeleted(true);
                    customer.setDeletionTime(LocalDateTime.now());
                    customer.setDeleterId(deleterId);
                    return customerRepository.save(customer);
                })
                .then();
    }
} 