package com.panda.mdmService.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class LoginHistoryUpdateRequest {
    private UUID userId;
    private LocalDateTime loginTime;
    private String ipAddress;
    private String device;
} 