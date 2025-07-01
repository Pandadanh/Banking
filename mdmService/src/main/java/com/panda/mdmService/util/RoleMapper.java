package com.panda.mdmService.util;

import com.panda.mdmService.model.Role;
import com.panda.mdmService.dto.RoleDto;
import com.panda.mdmService.dto.RoleCreateRequest;
import com.panda.mdmService.dto.RoleUpdateRequest;

public class RoleMapper {
    public static RoleDto toDto(Role role) {
        if (role == null) return null;
        RoleDto dto = new RoleDto();
        dto.setId(role.getId());
        dto.setName(role.getName());
        dto.setDescription(role.getDescription());
        return dto;
    }

    public static Role fromCreateRequest(RoleCreateRequest req) {
        Role role = new Role();
        role.setName(req.getName());
        role.setDescription(req.getDescription());
        return role;
    }

    public static void updateFromRequest(Role role, RoleUpdateRequest req) {
        if (req.getName() != null) role.setName(req.getName());
        if (req.getDescription() != null) role.setDescription(req.getDescription());
    }
} 