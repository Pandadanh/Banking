package com.panda.mdmService.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class StoreManagerDto {
    private UUID id;
    private UUID employeeId;
    private UUID branchId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime creationTime;
} 