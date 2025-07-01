package com.panda.mdmService.util;

import com.panda.mdmService.model.Department;
import com.panda.mdmService.dto.DepartmentDto;
import com.panda.mdmService.dto.DepartmentCreateRequest;
import com.panda.mdmService.dto.DepartmentUpdateRequest;

public class DepartmentMapper {
    public static DepartmentDto toDto(Department department) {
        if (department == null) return null;
        DepartmentDto dto = new DepartmentDto();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setDescription(department.getDescription());
        return dto;
    }

    public static Department fromCreateRequest(DepartmentCreateRequest req) {
        Department department = new Department();
        department.setName(req.getName());
        department.setDescription(req.getDescription());
        return department;
    }

    public static void updateFromRequest(Department department, DepartmentUpdateRequest req) {
        if (req.getName() != null) department.setName(req.getName());
        if (req.getDescription() != null) department.setDescription(req.getDescription());
    }
} 