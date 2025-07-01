package com.panda.mdmService.service;

import com.panda.mdmService.dto.ContractCreateRequest;
import com.panda.mdmService.dto.ContractDto;
import com.panda.mdmService.dto.ContractUpdateRequest;
import com.panda.mdmService.model.Contract;
import com.panda.mdmService.repository.ContractRepository;
import com.panda.mdmService.util.ContractMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContractService {
    private final ContractRepository contractRepository;

    public Flux<ContractDto> getAll() {
        return contractRepository.findAll().map(ContractMapper::toDto);
    }

    public Mono<ContractDto> getById(UUID id) {
        return contractRepository.findById(id).map(ContractMapper::toDto);
    }

    public Mono<ContractDto> create(ContractCreateRequest req, UUID creatorId) {
        Contract contract = ContractMapper.fromCreateRequest(req);
        contract.setId(UUID.randomUUID());
        contract.setCreationTime(LocalDateTime.now());
        contract.setCreatorId(creatorId);
        return contractRepository.save(contract).map(ContractMapper::toDto);
    }

    public Mono<ContractDto> update(UUID id, ContractUpdateRequest req, UUID modifierId) {
        return contractRepository.findById(id)
                .flatMap(contract -> {
                    ContractMapper.updateFromRequest(contract, req);
                    contract.setLastModificationTime(LocalDateTime.now());
                    contract.setLastModifierId(modifierId);
                    return contractRepository.save(contract);
                })
                .map(ContractMapper::toDto);
    }

    public Mono<Void> delete(UUID id, UUID deleterId) {
        return contractRepository.findById(id)
                .flatMap(contract -> {
                    contract.setIsDeleted(true);
                    contract.setDeletionTime(LocalDateTime.now());
                    contract.setDeleterId(deleterId);
                    return contractRepository.save(contract);
                })
                .then();
    }
} 