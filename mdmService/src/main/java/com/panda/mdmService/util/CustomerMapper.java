package com.panda.mdmService.util;

import com.panda.mdmService.model.Customer;
import com.panda.mdmService.dto.CustomerDto;
import com.panda.mdmService.dto.CustomerCreateRequest;
import com.panda.mdmService.dto.CustomerUpdateRequest;

public class CustomerMapper {
    public static CustomerDto toDto(Customer customer) {
        if (customer == null) return null;
        CustomerDto dto = new CustomerDto();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        dto.setPhone(customer.getPhone());
        dto.setAddress(customer.getAddress());
        dto.setDob(customer.getDob());
        dto.setIsActive(customer.getIsActive());
        dto.setCreationTime(customer.getCreationTime());
        return dto;
    }

    public static Customer fromCreateRequest(CustomerCreateRequest req) {
        Customer customer = new Customer();
        customer.setName(req.getName());
        customer.setEmail(req.getEmail());
        customer.setPhone(req.getPhone());
        customer.setAddress(req.getAddress());
        customer.setDob(req.getDob());
        customer.setIsActive(true);
        return customer;
    }

    public static void updateFromRequest(Customer customer, CustomerUpdateRequest req) {
        if (req.getName() != null) customer.setName(req.getName());
        if (req.getEmail() != null) customer.setEmail(req.getEmail());
        if (req.getPhone() != null) customer.setPhone(req.getPhone());
        if (req.getAddress() != null) customer.setAddress(req.getAddress());
        if (req.getDob() != null) customer.setDob(req.getDob());
        if (req.getIsActive() != null) customer.setIsActive(req.getIsActive());
    }
} 