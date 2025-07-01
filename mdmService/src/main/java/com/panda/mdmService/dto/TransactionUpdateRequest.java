package com.panda.mdmService.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TransactionUpdateRequest {
    private LocalDateTime date;
    private BigDecimal amount;
    private String type;
    private UUID accountId;
    private String description;
} 