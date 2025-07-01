package com.panda.mdmService.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ContractCreateRequest {
    private UUID customerId;
    private UUID productId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
} 