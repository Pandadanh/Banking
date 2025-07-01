package com.panda.mdmService.service;

import com.panda.mdmService.dto.RoleCreateRequest;
import com.panda.mdmService.dto.RoleDto;
import com.panda.mdmService.dto.RoleUpdateRequest;
import com.panda.mdmService.model.Role;
import com.panda.mdmService.repository.RoleRepository;
import com.panda.mdmService.util.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Flux<RoleDto> getAll() {
        return roleRepository.findAll().map(RoleMapper::toDto);
    }

    public Mono<RoleDto> getById(UUID id) {
        return roleRepository.findById(id).map(RoleMapper::toDto);
    }

    public Mono<RoleDto> create(RoleCreateRequest req) {
        Role role = RoleMapper.fromCreateRequest(req);
        role.setId(UUID.randomUUID());
        return roleRepository.save(role).map(RoleMapper::toDto);
    }

    public Mono<RoleDto> update(UUID id, RoleUpdateRequest req) {
        return roleRepository.findById(id)
                .flatMap(role -> {
                    RoleMapper.updateFromRequest(role, req);
                    return roleRepository.save(role);
                })
                .map(RoleMapper::toDto);
    }

    public Mono<Void> delete(UUID id) {
        return roleRepository.deleteById(id);
    }
} 