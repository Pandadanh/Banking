package com.panda.mdmService.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class EmployeeDto {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private LocalDateTime dob;
    private UUID branchId;
    private Boolean isActive;
    private LocalDateTime creationTime;
} 