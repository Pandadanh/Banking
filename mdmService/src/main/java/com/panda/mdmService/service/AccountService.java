package com.panda.mdmService.service;

import com.panda.mdmService.dto.AccountCreateRequest;
import com.panda.mdmService.dto.AccountDto;
import com.panda.mdmService.dto.AccountUpdateRequest;
import com.panda.mdmService.model.Account;
import com.panda.mdmService.repository.AccountRepository;
import com.panda.mdmService.util.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Flux<AccountDto> getAll() {
        return accountRepository.findAll().map(AccountMapper::toDto);
    }

    public Mono<AccountDto> getById(UUID id) {
        return accountRepository.findById(id).map(AccountMapper::toDto);
    }

    public Mono<AccountDto> create(AccountCreateRequest req, UUID creatorId) {
        Account account = AccountMapper.fromCreateRequest(req);
        account.setId(UUID.randomUUID());
        account.setCreationTime(LocalDateTime.now());
        account.setCreatorId(creatorId);
        return accountRepository.save(account).map(AccountMapper::toDto);
    }

    public Mono<AccountDto> update(UUID id, AccountUpdateRequest req, UUID modifierId) {
        return accountRepository.findById(id)
                .flatMap(account -> {
                    AccountMapper.updateFromRequest(account, req);
                    account.setLastModificationTime(LocalDateTime.now());
                    account.setLastModifierId(modifierId);
                    return accountRepository.save(account);
                })
                .map(AccountMapper::toDto);
    }

    public Mono<Void> delete(UUID id, UUID deleterId) {
        return accountRepository.findById(id)
                .flatMap(account -> {
                    account.setIsDeleted(true);
                    account.setDeletionTime(LocalDateTime.now());
                    account.setDeleterId(deleterId);
                    return accountRepository.save(account);
                })
                .then();
    }
} 