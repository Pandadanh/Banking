package com.panda.mdmService.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class StoreRouteDto {
    private UUID id;
    private UUID branchId;
    private String routeName;
    private String description;
    private LocalDateTime creationTime;
} 