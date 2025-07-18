package com.panda.mdmService.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PayrollCreateRequest {
    private UUID employeeId;
    private LocalDateTime payDate;
    private BigDecimal amount;
    private String note;
} 