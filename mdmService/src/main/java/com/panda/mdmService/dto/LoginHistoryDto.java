package com.panda.mdmService.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class LoginHistoryDto {
    private UUID id;
    private UUID userId;
    private LocalDateTime loginTime;
    private String ipAddress;
    private String device;
} 