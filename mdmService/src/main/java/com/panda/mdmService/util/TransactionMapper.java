package com.panda.mdmService.util;

import com.panda.mdmService.model.Transaction;
import com.panda.mdmService.dto.TransactionDto;
import com.panda.mdmService.dto.TransactionCreateRequest;
import com.panda.mdmService.dto.TransactionUpdateRequest;

public class TransactionMapper {
    public static TransactionDto toDto(Transaction transaction) {
        if (transaction == null) return null;
        TransactionDto dto = new TransactionDto();
        dto.setId(transaction.getId());
        dto.setDate(transaction.getDate());
        dto.setAmount(transaction.getAmount());
        dto.setType(transaction.getType());
        dto.setAccountId(transaction.getAccountId());
        dto.setDescription(transaction.getDescription());
        dto.setCreationTime(transaction.getCreationTime());
        return dto;
    }

    public static Transaction fromCreateRequest(TransactionCreateRequest req) {
        Transaction transaction = new Transaction();
        transaction.setDate(req.getDate());
        transaction.setAmount(req.getAmount());
        transaction.setType(req.getType());
        transaction.setAccountId(req.getAccountId());
        transaction.setDescription(req.getDescription());
        return transaction;
    }

    public static void updateFromRequest(Transaction transaction, TransactionUpdateRequest req) {
        if (req.getDate() != null) transaction.setDate(req.getDate());
        if (req.getAmount() != null) transaction.setAmount(req.getAmount());
        if (req.getType() != null) transaction.setType(req.getType());
        if (req.getAccountId() != null) transaction.setAccountId(req.getAccountId());
        if (req.getDescription() != null) transaction.setDescription(req.getDescription());
    }
} 