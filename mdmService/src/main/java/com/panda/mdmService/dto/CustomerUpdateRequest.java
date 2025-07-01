package com.panda.mdmService.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CustomerUpdateRequest {
    private String name;
    private String email;
    private String phone;
    private String address;
    private LocalDateTime dob;
    private Boolean isActive;
} 