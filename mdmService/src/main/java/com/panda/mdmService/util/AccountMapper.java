package com.panda.mdmService.util;

import com.panda.mdmService.model.Account;
import com.panda.mdmService.dto.AccountDto;
import com.panda.mdmService.dto.AccountCreateRequest;
import com.panda.mdmService.dto.AccountUpdateRequest;

public class AccountMapper {
    public static AccountDto toDto(Account account) {
        if (account == null) return null;
        AccountDto dto = new AccountDto();
        dto.setId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setBalance(account.getBalance());
        dto.setCustomerId(account.getCustomerId());
        dto.setBranchId(account.getBranchId());
        dto.setOpenDate(account.getOpenDate());
        dto.setStatus(account.getStatus());
        dto.setCreationTime(account.getCreationTime());
        return dto;
    }

    public static Account fromCreateRequest(AccountCreateRequest req) {
        Account account = new Account();
        account.setAccountNumber(req.getAccountNumber());
        account.setBalance(req.getBalance());
        account.setCustomerId(req.getCustomerId());
        account.setBranchId(req.getBranchId());
        account.setOpenDate(req.getOpenDate());
        account.setStatus(req.getStatus());
        return account;
    }

    public static void updateFromRequest(Account account, AccountUpdateRequest req) {
        if (req.getAccountNumber() != null) account.setAccountNumber(req.getAccountNumber());
        if (req.getBalance() != null) account.setBalance(req.getBalance());
        if (req.getCustomerId() != null) account.setCustomerId(req.getCustomerId());
        if (req.getBranchId() != null) account.setBranchId(req.getBranchId());
        if (req.getOpenDate() != null) account.setOpenDate(req.getOpenDate());
        if (req.getStatus() != null) account.setStatus(req.getStatus());
    }
} 