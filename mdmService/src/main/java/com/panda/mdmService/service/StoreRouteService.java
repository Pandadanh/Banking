package com.panda.mdmService.service;

import com.panda.mdmService.dto.StoreRouteCreateRequest;
import com.panda.mdmService.dto.StoreRouteDto;
import com.panda.mdmService.dto.StoreRouteUpdateRequest;
import com.panda.mdmService.model.StoreRoute;
import com.panda.mdmService.repository.StoreRouteRepository;
import com.panda.mdmService.util.StoreRouteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreRouteService {
    private final StoreRouteRepository storeRouteRepository;

    public Flux<StoreRouteDto> getAll() {
        return storeRouteRepository.findAll().map(StoreRouteMapper::toDto);
    }

    public Mono<StoreRouteDto> getById(UUID id) {
        return storeRouteRepository.findById(id).map(StoreRouteMapper::toDto);
    }

    public Mono<StoreRouteDto> create(StoreRouteCreateRequest req, UUID creatorId) {
        StoreRoute storeRoute = StoreRouteMapper.fromCreateRequest(req);
        storeRoute.setId(UUID.randomUUID());
        storeRoute.setCreationTime(LocalDateTime.now());
        storeRoute.setCreatorId(creatorId);
        return storeRouteRepository.save(storeRoute).map(StoreRouteMapper::toDto);
    }

    public Mono<StoreRouteDto> update(UUID id, StoreRouteUpdateRequest req, UUID modifierId) {
        return storeRouteRepository.findById(id)
                .flatMap(storeRoute -> {
                    StoreRouteMapper.updateFromRequest(storeRoute, req);
                    storeRoute.setLastModificationTime(LocalDateTime.now());
                    storeRoute.setLastModifierId(modifierId);
                    return storeRouteRepository.save(storeRoute);
                })
                .map(StoreRouteMapper::toDto);
    }

    public Mono<Void> delete(UUID id, UUID deleterId) {
        return storeRouteRepository.findById(id)
                .flatMap(storeRoute -> {
                    storeRoute.setIsDeleted(true);
                    storeRoute.setDeletionTime(LocalDateTime.now());
                    storeRoute.setDeleterId(deleterId);
                    return storeRouteRepository.save(storeRoute);
                })
                .then();
    }
} 