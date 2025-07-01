package com.panda.mdmService.util;

import com.panda.mdmService.model.Address;
import com.panda.mdmService.dto.AddressDto;
import com.panda.mdmService.dto.AddressCreateRequest;
import com.panda.mdmService.dto.AddressUpdateRequest;

public class AddressMapper {
    public static AddressDto toDto(Address address) {
        if (address == null) return null;
        AddressDto dto = new AddressDto();
        dto.setId(address.getId());
        dto.setStreet(address.getStreet());
        dto.setCity(address.getCity());
        dto.setDistrict(address.getDistrict());
        dto.setProvince(address.getProvince());
        dto.setCountry(address.getCountry());
        dto.setPostalCode(address.getPostalCode());
        return dto;
    }

    public static Address fromCreateRequest(AddressCreateRequest req) {
        Address address = new Address();
        address.setStreet(req.getStreet());
        address.setCity(req.getCity());
        address.setDistrict(req.getDistrict());
        address.setProvince(req.getProvince());
        address.setCountry(req.getCountry());
        address.setPostalCode(req.getPostalCode());
        return address;
    }

    public static void updateFromRequest(Address address, AddressUpdateRequest req) {
        if (req.getStreet() != null) address.setStreet(req.getStreet());
        if (req.getCity() != null) address.setCity(req.getCity());
        if (req.getDistrict() != null) address.setDistrict(req.getDistrict());
        if (req.getProvince() != null) address.setProvince(req.getProvince());
        if (req.getCountry() != null) address.setCountry(req.getCountry());
        if (req.getPostalCode() != null) address.setPostalCode(req.getPostalCode());
    }
} 