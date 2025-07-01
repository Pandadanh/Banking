package com.panda.mdmService.service;

import com.panda.mdmService.dto.AddressCreateRequest;
import com.panda.mdmService.dto.AddressDto;
import com.panda.mdmService.dto.AddressUpdateRequest;
import com.panda.mdmService.model.Address;
import com.panda.mdmService.repository.AddressRepository;
import com.panda.mdmService.util.AddressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    public Flux<AddressDto> getAll() {
        return addressRepository.findAll().map(AddressMapper::toDto);
    }

    public Mono<AddressDto> getById(UUID id) {
        return addressRepository.findById(id).map(AddressMapper::toDto);
    }

    public Mono<AddressDto> create(AddressCreateRequest req) {
        Address address = AddressMapper.fromCreateRequest(req);
        address.setId(UUID.randomUUID());
        return addressRepository.save(address).map(AddressMapper::toDto);
    }

    public Mono<AddressDto> update(UUID id, AddressUpdateRequest req) {
        return addressRepository.findById(id)
                .flatMap(address -> {
                    AddressMapper.updateFromRequest(address, req);
                    return addressRepository.save(address);
                })
                .map(AddressMapper::toDto);
    }

    public Mono<Void> delete(UUID id) {
        return addressRepository.deleteById(id);
    }
} 