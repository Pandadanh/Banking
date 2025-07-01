package com.panda.mdmService.util;

import com.panda.mdmService.model.Branch;
import com.panda.mdmService.dto.BranchDto;
import com.panda.mdmService.dto.BranchCreateRequest;
import com.panda.mdmService.dto.BranchUpdateRequest;

public class BranchMapper {
    public static BranchDto toDto(Branch branch) {
        if (branch == null) return null;
        BranchDto dto = new BranchDto();
        dto.setId(branch.getId());
        dto.setName(branch.getName());
        dto.setAddressId(branch.getAddressId());
        dto.setManagerId(branch.getManagerId());
        dto.setCreationTime(branch.getCreationTime());
        return dto;
    }

    public static Branch fromCreateRequest(BranchCreateRequest req) {
        Branch branch = new Branch();
        branch.setName(req.getName());
        branch.setAddressId(req.getAddressId());
        branch.setManagerId(req.getManagerId());
        return branch;
    }

    public static void updateFromRequest(Branch branch, BranchUpdateRequest req) {
        if (req.getName() != null) branch.setName(req.getName());
        if (req.getAddressId() != null) branch.setAddressId(req.getAddressId());
        if (req.getManagerId() != null) branch.setManagerId(req.getManagerId());
    }
} 