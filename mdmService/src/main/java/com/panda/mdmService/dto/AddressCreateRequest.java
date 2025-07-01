package com.panda.mdmService.dto;

import lombok.Data;

@Data
public class AddressCreateRequest {
    private String street;
    private String city;
    private String district;
    private String province;
    private String country;
    private String postalCode;
} 