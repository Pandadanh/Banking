package com.panda.mdmService.util;

import com.panda.mdmService.model.StoreManager;
import com.panda.mdmService.dto.StoreManagerDto;
import com.panda.mdmService.dto.StoreManagerCreateRequest;
import com.panda.mdmService.dto.StoreManagerUpdateRequest;

public class StoreManagerMapper {
    public static StoreManagerDto toDto(StoreManager storeManager) {
        if (storeManager == null) return null;
        StoreManagerDto dto = new StoreManagerDto();
        dto.setId(storeManager.getId());
        dto.setEmployeeId(storeManager.getEmployeeId());
        dto.setBranchId(storeManager.getBranchId());
        dto.setStartDate(storeManager.getStartDate());
        dto.setEndDate(storeManager.getEndDate());
        dto.setCreationTime(storeManager.getCreationTime());
        return dto;
    }

    public static StoreManager fromCreateRequest(StoreManagerCreateRequest req) {
        StoreManager storeManager = new StoreManager();
        storeManager.setEmployeeId(req.getEmployeeId());
        storeManager.setBranchId(req.getBranchId());
        storeManager.setStartDate(req.getStartDate());
        storeManager.setEndDate(req.getEndDate());
        return storeManager;
    }

    public static void updateFromRequest(StoreManager storeManager, StoreManagerUpdateRequest req) {
        if (req.getEmployeeId() != null) storeManager.setEmployeeId(req.getEmployeeId());
        if (req.getBranchId() != null) storeManager.setBranchId(req.getBranchId());
        if (req.getStartDate() != null) storeManager.setStartDate(req.getStartDate());
        if (req.getEndDate() != null) storeManager.setEndDate(req.getEndDate());
    }
} 