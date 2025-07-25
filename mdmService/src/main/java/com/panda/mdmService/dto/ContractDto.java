package com.panda.mdmService.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ContractDto {
    private UUID id;
    private UUID customerId;
    private UUID productId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
    private LocalDateTime creationTime;
} 