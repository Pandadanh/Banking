package com.panda.mdmService.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CardUpdateRequest {
    private String cardNumber;
    private UUID accountId;
    private LocalDateTime issueDate;
    private LocalDateTime expiryDate;
    private String type;
    private String status;
} 