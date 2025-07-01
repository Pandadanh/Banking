package com.panda.mdmService.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class BranchUpdateRequest {
    private String name;
    private UUID addressId;
    private UUID managerId;
} 