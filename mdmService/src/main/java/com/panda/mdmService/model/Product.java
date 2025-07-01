package com.panda.mdmService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("PRODUCTS")
public class Product {
    @Id
    @Column("Id")
    private UUID id;

    @Column("Name")
    private String name;

    @Column("Type")
    private String type;

    @Column("Price")
    private BigDecimal price;

    @Column("Description")
    private String description;
} 