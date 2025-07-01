package com.panda.mdmService.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class RoleDto {
    private UUID id;
    private String name;
    private String description;
} 