package com.panda.mdmService.controller;

import com.panda.mdmService.dto.CardCreateRequest;
import com.panda.mdmService.dto.CardDto;
import com.panda.mdmService.dto.CardUpdateRequest;
import com.panda.mdmService.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @GetMapping
    public Flux<CardDto> getAll() {
        return cardService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<CardDto> getById(@PathVariable UUID id) {
        return cardService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CardDto> create(@RequestBody CardCreateRequest req, @RequestHeader("X-User-Id") UUID creatorId) {
        return cardService.create(req, creatorId);
    }

    @PutMapping("/{id}")
    public Mono<CardDto> update(@PathVariable UUID id, @RequestBody CardUpdateRequest req, @RequestHeader("X-User-Id") UUID modifierId) {
        return cardService.update(id, req, modifierId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable UUID id, @RequestHeader("X-User-Id") UUID deleterId) {
        return cardService.delete(id, deleterId);
    }
} 