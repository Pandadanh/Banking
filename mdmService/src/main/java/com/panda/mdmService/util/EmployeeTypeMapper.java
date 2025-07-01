package com.panda.mdmService.util;

import com.panda.mdmService.model.EmployeeType;
import com.panda.mdmService.dto.EmployeeTypeDto;
import com.panda.mdmService.dto.EmployeeTypeCreateRequest;
import com.panda.mdmService.dto.EmployeeTypeUpdateRequest;

public class EmployeeTypeMapper {
    public static EmployeeTypeDto toDto(EmployeeType employeeType) {
        if (employeeType == null) return null;
        EmployeeTypeDto dto = new EmployeeTypeDto();
        dto.setId(employeeType.getId());
        dto.setName(employeeType.getName());
        dto.setDescription(employeeType.getDescription());
        return dto;
    }

    public static EmployeeType fromCreateRequest(EmployeeTypeCreateRequest req) {
        EmployeeType employeeType = new EmployeeType();
        employeeType.setName(req.getName());
        employeeType.setDescription(req.getDescription());
        return employeeType;
    }

    public static void updateFromRequest(EmployeeType employeeType, EmployeeTypeUpdateRequest req) {
        if (req.getName() != null) employeeType.setName(req.getName());
        if (req.getDescription() != null) employeeType.setDescription(req.getDescription());
    }
} 