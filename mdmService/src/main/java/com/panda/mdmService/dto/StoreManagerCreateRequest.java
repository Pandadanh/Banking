package com.panda.mdmService.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class StoreManagerCreateRequest {
    private UUID employeeId;
    private UUID branchId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
} 