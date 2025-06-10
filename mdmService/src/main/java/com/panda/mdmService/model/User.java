package com.panda.mdmService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String userId;
    private String phone;
    private String tenantId;
    private String tenantCode;
    private String employeeId;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String type;
} 