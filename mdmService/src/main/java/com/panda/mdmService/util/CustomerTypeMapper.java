package com.panda.mdmService.util;

import com.panda.mdmService.model.CustomerType;
import com.panda.mdmService.dto.CustomerTypeDto;
import com.panda.mdmService.dto.CustomerTypeCreateRequest;
import com.panda.mdmService.dto.CustomerTypeUpdateRequest;

public class CustomerTypeMapper {
    public static CustomerTypeDto toDto(CustomerType customerType) {
        if (customerType == null) return null;
        CustomerTypeDto dto = new CustomerTypeDto();
        dto.setId(customerType.getId());
        dto.setName(customerType.getName());
        dto.setDescription(customerType.getDescription());
        return dto;
    }

    public static CustomerType fromCreateRequest(CustomerTypeCreateRequest req) {
        CustomerType customerType = new CustomerType();
        customerType.setName(req.getName());
        customerType.setDescription(req.getDescription());
        return customerType;
    }

    public static void updateFromRequest(CustomerType customerType, CustomerTypeUpdateRequest req) {
        if (req.getName() != null) customerType.setName(req.getName());
        if (req.getDescription() != null) customerType.setDescription(req.getDescription());
    }
} 