package com.panda.mdmService.util;

import com.panda.mdmService.model.Product;
import com.panda.mdmService.dto.ProductDto;
import com.panda.mdmService.dto.ProductCreateRequest;
import com.panda.mdmService.dto.ProductUpdateRequest;

public class ProductMapper {
    public static ProductDto toDto(Product product) {
        if (product == null) return null;
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setType(product.getType());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());
        return dto;
    }

    public static Product fromCreateRequest(ProductCreateRequest req) {
        Product product = new Product();
        product.setName(req.getName());
        product.setType(req.getType());
        product.setPrice(req.getPrice());
        product.setDescription(req.getDescription());
        return product;
    }

    public static void updateFromRequest(Product product, ProductUpdateRequest req) {
        if (req.getName() != null) product.setName(req.getName());
        if (req.getType() != null) product.setType(req.getType());
        if (req.getPrice() != null) product.setPrice(req.getPrice());
        if (req.getDescription() != null) product.setDescription(req.getDescription());
    }
} 