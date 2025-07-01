package com.panda.mdmService.service;

import com.panda.mdmService.dto.CardCreateRequest;
import com.panda.mdmService.dto.CardDto;
import com.panda.mdmService.dto.CardUpdateRequest;
import com.panda.mdmService.model.Card;
import com.panda.mdmService.repository.CardRepository;
import com.panda.mdmService.util.CardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;

    public Flux<CardDto> getAll() {
        return cardRepository.findAll().map(CardMapper::toDto);
    }

    public Mono<CardDto> getById(UUID id) {
        return cardRepository.findById(id).map(CardMapper::toDto);
    }

    public Mono<CardDto> create(CardCreateRequest req, UUID creatorId) {
        Card card = CardMapper.fromCreateRequest(req);
        card.setId(UUID.randomUUID());
        card.setCreationTime(LocalDateTime.now());
        card.setCreatorId(creatorId);
        return cardRepository.save(card).map(CardMapper::toDto);
    }

    public Mono<CardDto> update(UUID id, CardUpdateRequest req, UUID modifierId) {
        return cardRepository.findById(id)
                .flatMap(card -> {
                    CardMapper.updateFromRequest(card, req);
                    card.setLastModificationTime(LocalDateTime.now());
                    card.setLastModifierId(modifierId);
                    return cardRepository.save(card);
                })
                .map(CardMapper::toDto);
    }

    public Mono<Void> delete(UUID id, UUID deleterId) {
        return cardRepository.findById(id)
                .flatMap(card -> {
                    card.setIsDeleted(true);
                    card.setDeletionTime(LocalDateTime.now());
                    card.setDeleterId(deleterId);
                    return cardRepository.save(card);
                })
                .then();
    }
} 