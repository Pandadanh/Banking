package com.panda.mdmService.util;

import com.panda.mdmService.model.Contract;
import com.panda.mdmService.dto.ContractDto;
import com.panda.mdmService.dto.ContractCreateRequest;
import com.panda.mdmService.dto.ContractUpdateRequest;

public class ContractMapper {
    public static ContractDto toDto(Contract contract) {
        if (contract == null) return null;
        ContractDto dto = new ContractDto();
        dto.setId(contract.getId());
        dto.setCustomerId(contract.getCustomerId());
        dto.setProductId(contract.getProductId());
        dto.setStartDate(contract.getStartDate());
        dto.setEndDate(contract.getEndDate());
        dto.setStatus(contract.getStatus());
        dto.setCreationTime(contract.getCreationTime());
        return dto;
    }

    public static Contract fromCreateRequest(ContractCreateRequest req) {
        Contract contract = new Contract();
        contract.setCustomerId(req.getCustomerId());
        contract.setProductId(req.getProductId());
        contract.setStartDate(req.getStartDate());
        contract.setEndDate(req.getEndDate());
        contract.setStatus(req.getStatus());
        return contract;
    }

    public static void updateFromRequest(Contract contract, ContractUpdateRequest req) {
        if (req.getCustomerId() != null) contract.setCustomerId(req.getCustomerId());
        if (req.getProductId() != null) contract.setProductId(req.getProductId());
        if (req.getStartDate() != null) contract.setStartDate(req.getStartDate());
        if (req.getEndDate() != null) contract.setEndDate(req.getEndDate());
        if (req.getStatus() != null) contract.setStatus(req.getStatus());
    }
} 