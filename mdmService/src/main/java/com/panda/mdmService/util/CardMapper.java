package com.panda.mdmService.util;

import com.panda.mdmService.model.Card;
import com.panda.mdmService.dto.CardDto;
import com.panda.mdmService.dto.CardCreateRequest;
import com.panda.mdmService.dto.CardUpdateRequest;

public class CardMapper {
    public static CardDto toDto(Card card) {
        if (card == null) return null;
        CardDto dto = new CardDto();
        dto.setId(card.getId());
        dto.setCardNumber(card.getCardNumber());
        dto.setAccountId(card.getAccountId());
        dto.setIssueDate(card.getIssueDate());
        dto.setExpiryDate(card.getExpiryDate());
        dto.setType(card.getType());
        dto.setStatus(card.getStatus());
        dto.setCreationTime(card.getCreationTime());
        return dto;
    }

    public static Card fromCreateRequest(CardCreateRequest req) {
        Card card = new Card();
        card.setCardNumber(req.getCardNumber());
        card.setAccountId(req.getAccountId());
        card.setIssueDate(req.getIssueDate());
        card.setExpiryDate(req.getExpiryDate());
        card.setType(req.getType());
        card.setStatus(req.getStatus());
        return card;
    }

    public static void updateFromRequest(Card card, CardUpdateRequest req) {
        if (req.getCardNumber() != null) card.setCardNumber(req.getCardNumber());
        if (req.getAccountId() != null) card.setAccountId(req.getAccountId());
        if (req.getIssueDate() != null) card.setIssueDate(req.getIssueDate());
        if (req.getExpiryDate() != null) card.setExpiryDate(req.getExpiryDate());
        if (req.getType() != null) card.setType(req.getType());
        if (req.getStatus() != null) card.setStatus(req.getStatus());
    }
} 