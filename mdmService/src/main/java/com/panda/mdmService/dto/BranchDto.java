package com.panda.mdmService.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BranchDto {
    private UUID id;
    private String name;
    private UUID addressId;
    private UUID managerId;
    private LocalDateTime creationTime;
} 