package com.panda.mdmService.service;

import com.panda.mdmService.dto.StoreManagerCreateRequest;
import com.panda.mdmService.dto.StoreManagerDto;
import com.panda.mdmService.dto.StoreManagerUpdateRequest;
import com.panda.mdmService.model.StoreManager;
import com.panda.mdmService.repository.StoreManagerRepository;
import com.panda.mdmService.util.StoreManagerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreManagerService {
    private final StoreManagerRepository storeManagerRepository;

    public Flux<StoreManagerDto> getAll() {
        return storeManagerRepository.findAll().map(StoreManagerMapper::toDto);
    }

    public Mono<StoreManagerDto> getById(UUID id) {
        return storeManagerRepository.findById(id).map(StoreManagerMapper::toDto);
    }

    public Mono<StoreManagerDto> create(StoreManagerCreateRequest req, UUID creatorId) {
        StoreManager storeManager = StoreManagerMapper.fromCreateRequest(req);
        storeManager.setId(UUID.randomUUID());
        storeManager.setCreationTime(LocalDateTime.now());
        storeManager.setCreatorId(creatorId);
        return storeManagerRepository.save(storeManager).map(StoreManagerMapper::toDto);
    }

    public Mono<StoreManagerDto> update(UUID id, StoreManagerUpdateRequest req, UUID modifierId) {
        return storeManagerRepository.findById(id)
                .flatMap(storeManager -> {
                    StoreManagerMapper.updateFromRequest(storeManager, req);
                    storeManager.setLastModificationTime(LocalDateTime.now());
                    storeManager.setLastModifierId(modifierId);
                    return storeManagerRepository.save(storeManager);
                })
                .map(StoreManagerMapper::toDto);
    }

    public Mono<Void> delete(UUID id, UUID deleterId) {
        return storeManagerRepository.findById(id)
                .flatMap(storeManager -> {
                    storeManager.setIsDeleted(true);
                    storeManager.setDeletionTime(LocalDateTime.now());
                    storeManager.setDeleterId(deleterId);
                    return storeManagerRepository.save(storeManager);
                })
                .then();
    }
} 