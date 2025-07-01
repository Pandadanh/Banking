package com.panda.mdmService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("ADDRESSES")
public class Address {
    @Id
    @Column("Id")
    private UUID id;

    @Column("Street")
    private String street;

    @Column("City")
    private String city;

    @Column("District")
    private String district;

    @Column("Province")
    private String province;

    @Column("Country")
    private String country;

    @Column("PostalCode")
    private String postalCode;
} 