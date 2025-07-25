package com.panda.mdmService.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class StoreRouteUpdateRequest {
    private UUID branchId;
    private String routeName;
    private String description;
} 