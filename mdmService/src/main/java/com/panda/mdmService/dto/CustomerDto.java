package com.panda.mdmService.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CustomerDto {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private LocalDateTime dob;
    private Boolean isActive;
    private LocalDateTime creationTime;
} 