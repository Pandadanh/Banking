package com.panda.mdmService.service;

import com.panda.mdmService.dto.CreditProfileCreateRequest;
import com.panda.mdmService.dto.CreditProfileDto;
import com.panda.mdmService.dto.CreditProfileUpdateRequest;
import com.panda.mdmService.model.CreditProfile;
import com.panda.mdmService.repository.CreditProfileRepository;
import com.panda.mdmService.util.CreditProfileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreditProfileService {
    private final CreditProfileRepository creditProfileRepository;

    public Flux<CreditProfileDto> getAll() {
        return creditProfileRepository.findAll().map(CreditProfileMapper::toDto);
    }

    public Mono<CreditProfileDto> getById(UUID id) {
        return creditProfileRepository.findById(id).map(CreditProfileMapper::toDto);
    }

    public Mono<CreditProfileDto> create(CreditProfileCreateRequest req) {
        CreditProfile creditProfile = CreditProfileMapper.fromCreateRequest(req);
        creditProfile.setId(UUID.randomUUID());
        return creditProfileRepository.save(creditProfile).map(CreditProfileMapper::toDto);
    }

    public Mono<CreditProfileDto> update(UUID id, CreditProfileUpdateRequest req) {
        return creditProfileRepository.findById(id)
                .flatMap(creditProfile -> {
                    CreditProfileMapper.updateFromRequest(creditProfile, req);
                    return creditProfileRepository.save(creditProfile);
                })
                .map(CreditProfileMapper::toDto);
    }

    public Mono<Void> delete(UUID id) {
        return creditProfileRepository.deleteById(id);
    }
} 