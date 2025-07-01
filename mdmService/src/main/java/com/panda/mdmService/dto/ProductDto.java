package com.panda.mdmService.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductDto {
    private UUID id;
    private String name;
    private String type;
    private BigDecimal price;
    private String description;
} 