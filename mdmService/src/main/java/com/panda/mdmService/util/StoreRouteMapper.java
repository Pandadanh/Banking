package com.panda.mdmService.util;

import com.panda.mdmService.model.StoreRoute;
import com.panda.mdmService.dto.StoreRouteDto;
import com.panda.mdmService.dto.StoreRouteCreateRequest;
import com.panda.mdmService.dto.StoreRouteUpdateRequest;

public class StoreRouteMapper {
    public static StoreRouteDto toDto(StoreRoute storeRoute) {
        if (storeRoute == null) return null;
        StoreRouteDto dto = new StoreRouteDto();
        dto.setId(storeRoute.getId());
        dto.setBranchId(storeRoute.getBranchId());
        dto.setRouteName(storeRoute.getRouteName());
        dto.setDescription(storeRoute.getDescription());
        dto.setCreationTime(storeRoute.getCreationTime());
        return dto;
    }

    public static StoreRoute fromCreateRequest(StoreRouteCreateRequest req) {
        StoreRoute storeRoute = new StoreRoute();
        storeRoute.setBranchId(req.getBranchId());
        storeRoute.setRouteName(req.getRouteName());
        storeRoute.setDescription(req.getDescription());
        return storeRoute;
    }

    public static void updateFromRequest(StoreRoute storeRoute, StoreRouteUpdateRequest req) {
        if (req.getBranchId() != null) storeRoute.setBranchId(req.getBranchId());
        if (req.getRouteName() != null) storeRoute.setRouteName(req.getRouteName());
        if (req.getDescription() != null) storeRoute.setDescription(req.getDescription());
    }
} 