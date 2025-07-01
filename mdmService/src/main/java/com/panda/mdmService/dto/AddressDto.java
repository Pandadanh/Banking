package com.panda.mdmService.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class AddressDto {
    private UUID id;
    private String street;
    private String city;
    private String district;
    private String province;
    private String country;
    private String postalCode;
} 