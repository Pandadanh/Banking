package com.panda.mdmService.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class EmployeeTypeDto {
    private UUID id;
    private String name;
    private String description;
} 