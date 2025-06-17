package com.panda.mdmService.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @NotBlank(message = "User ID is required")
    private String userId;

    @Pattern(regexp = "^[0-9]{10,11}$", message = "Phone number must be 10-11 digits")
    private String phone;

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    @NotBlank(message = "Tenant code is required")
    private String tenantCode;

    private String employeeId;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String type;
}