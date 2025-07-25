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
@Table("DEPARTMENTS")
public class Department {
    @Id
    @Column("Id")
    private UUID id;

    @Column("Name")
    private String name;

    @Column("Description")
    private String description;
} 