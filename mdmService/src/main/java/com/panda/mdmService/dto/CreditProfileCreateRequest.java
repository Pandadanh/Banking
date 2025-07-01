package com.panda.mdmService.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreditProfileCreateRequest {
    private UUID customerId;
    private BigDecimal creditScore;
    private String riskLevel;
    private String note;
} 