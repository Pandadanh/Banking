package com.panda.mdmService.util;

import com.panda.mdmService.model.Employee;
import com.panda.mdmService.dto.EmployeeDto;
import com.panda.mdmService.dto.EmployeeCreateRequest;
import com.panda.mdmService.dto.EmployeeUpdateRequest;

public class EmployeeMapper {
    public static EmployeeDto toDto(Employee employee) {
        if (employee == null) return null;
        EmployeeDto dto = new EmployeeDto();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setEmail(employee.getEmail());
        dto.setPhone(employee.getPhone());
        dto.setDob(employee.getDob());
        dto.setBranchId(employee.getBranchId());
        dto.setIsActive(employee.getIsActive());
        dto.setCreationTime(employee.getCreationTime());
        return dto;
    }

    public static Employee fromCreateRequest(EmployeeCreateRequest req) {
        Employee employee = new Employee();
        employee.setName(req.getName());
        employee.setEmail(req.getEmail());
        employee.setPhone(req.getPhone());
        employee.setDob(req.getDob());
        employee.setBranchId(req.getBranchId());
        employee.setIsActive(true);
        return employee;
    }

    public static void updateFromRequest(Employee employee, EmployeeUpdateRequest req) {
        if (req.getName() != null) employee.setName(req.getName());
        if (req.getEmail() != null) employee.setEmail(req.getEmail());
        if (req.getPhone() != null) employee.setPhone(req.getPhone());
        if (req.getDob() != null) employee.setDob(req.getDob());
        if (req.getBranchId() != null) employee.setBranchId(req.getBranchId());
        if (req.getIsActive() != null) employee.setIsActive(req.getIsActive());
    }
} 