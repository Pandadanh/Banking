package com.panda.mdmService.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AccountDto {
    private UUID id;
    private String accountNumber;
    private BigDecimal balance;
    private UUID customerId;
    private UUID branchId;
    private LocalDateTime openDate;
    private String status;
    private LocalDateTime creationTime;
} 