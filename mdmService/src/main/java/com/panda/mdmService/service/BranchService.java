package com.panda.mdmService.service;

import com.panda.mdmService.dto.BranchCreateRequest;
import com.panda.mdmService.dto.BranchDto;
import com.panda.mdmService.dto.BranchUpdateRequest;
import com.panda.mdmService.model.Branch;
import com.panda.mdmService.repository.BranchRepository;
import com.panda.mdmService.util.BranchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BranchService {
    private final BranchRepository branchRepository;

    public Flux<BranchDto> getAll() {
        return branchRepository.findAll().map(BranchMapper::toDto);
    }

    public Mono<BranchDto> getById(UUID id) {
        return branchRepository.findById(id).map(BranchMapper::toDto);
    }

    public Mono<BranchDto> create(BranchCreateRequest req, UUID creatorId) {
        Branch branch = BranchMapper.fromCreateRequest(req);
        branch.setId(UUID.randomUUID());
        branch.setCreationTime(LocalDateTime.now());
        branch.setCreatorId(creatorId);
        return branchRepository.save(branch).map(BranchMapper::toDto);
    }

    public Mono<BranchDto> update(UUID id, BranchUpdateRequest req, UUID modifierId) {
        return branchRepository.findById(id)
                .flatMap(branch -> {
                    BranchMapper.updateFromRequest(branch, req);
                    branch.setLastModificationTime(LocalDateTime.now());
                    branch.setLastModifierId(modifierId);
                    return branchRepository.save(branch);
                })
                .map(BranchMapper::toDto);
    }

    public Mono<Void> delete(UUID id, UUID deleterId) {
        return branchRepository.findById(id)
                .flatMap(branch -> {
                    branch.setIsDeleted(true);
                    branch.setDeletionTime(LocalDateTime.now());
                    branch.setDeleterId(deleterId);
                    return branchRepository.save(branch);
                })
                .then();
    }
} 