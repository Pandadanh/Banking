package com.panda.mdmService.controller;

import com.panda.mdmService.dto.AddressCreateRequest;
import com.panda.mdmService.dto.AddressDto;
import com.panda.mdmService.dto.AddressUpdateRequest;
import com.panda.mdmService.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping
    public Flux<AddressDto> getAll() {
        return addressService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<AddressDto> getById(@PathVariable UUID id) {
        return addressService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AddressDto> create(@RequestBody AddressCreateRequest req) {
        return addressService.create(req);
    }

    @PutMapping("/{id}")
    public Mono<AddressDto> update(@PathVariable UUID id, @RequestBody AddressUpdateRequest req) {
        return addressService.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable UUID id) {
        return addressService.delete(id);
    }
} 