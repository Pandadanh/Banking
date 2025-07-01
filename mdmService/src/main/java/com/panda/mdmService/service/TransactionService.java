package com.panda.mdmService.service;

import com.panda.mdmService.dto.TransactionCreateRequest;
import com.panda.mdmService.dto.TransactionDto;
import com.panda.mdmService.dto.TransactionUpdateRequest;
import com.panda.mdmService.model.Transaction;
import com.panda.mdmService.repository.TransactionRepository;
import com.panda.mdmService.util.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public Flux<TransactionDto> getAll() {
        return transactionRepository.findAll().map(TransactionMapper::toDto);
    }

    public Mono<TransactionDto> getById(UUID id) {
        return transactionRepository.findById(id).map(TransactionMapper::toDto);
    }

    public Mono<TransactionDto> create(TransactionCreateRequest req, UUID creatorId) {
        Transaction transaction = TransactionMapper.fromCreateRequest(req);
        transaction.setId(UUID.randomUUID());
        transaction.setCreationTime(LocalDateTime.now());
        transaction.setCreatorId(creatorId);
        return transactionRepository.save(transaction).map(TransactionMapper::toDto);
    }

    public Mono<TransactionDto> update(UUID id, TransactionUpdateRequest req, UUID modifierId) {
        return transactionRepository.findById(id)
                .flatMap(transaction -> {
                    TransactionMapper.updateFromRequest(transaction, req);
                    transaction.setLastModificationTime(LocalDateTime.now());
                    transaction.setLastModifierId(modifierId);
                    return transactionRepository.save(transaction);
                })
                .map(TransactionMapper::toDto);
    }

    public Mono<Void> delete(UUID id, UUID deleterId) {
        return transactionRepository.findById(id)
                .flatMap(transaction -> {
                    transaction.setIsDeleted(true);
                    transaction.setDeletionTime(LocalDateTime.now());
                    transaction.setDeleterId(deleterId);
                    return transactionRepository.save(transaction);
                })
                .then();
    }
} 