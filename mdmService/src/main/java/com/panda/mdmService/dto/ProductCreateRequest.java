package com.panda.mdmService.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductCreateRequest {
    private String name;
    private String type;
    private BigDecimal price;
    private String description;
} 