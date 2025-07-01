package com.panda.mdmService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("EMPLOYEES")
public class Employee {
    @Id
    @Column("Id")
    private UUID id;

    @Column("Name")
    private String name;

    @Column("Email")
    private String email;

    @Column("Phone")
    private String phone;

    @Column("Dob")
    private LocalDateTime dob;

    @Column("BranchId")
    private UUID branchId;

    @Column("IsActive")
    private Boolean isActive = true;

    @Column("CreationTime")
    private LocalDateTime creationTime;

    @Column("CreatorId")
    private UUID creatorId;

    @Column("LastModificationTime")
    private LocalDateTime lastModificationTime;

    @Column("LastModifierId")
    private UUID lastModifierId;

    @Column("IsDeleted")
    private Boolean isDeleted = false;

    @Column("DeleterId")
    private UUID deleterId;

    @Column("DeletionTime")
    private LocalDateTime deletionTime;

    @Column("EntityVersion")
    private Integer entityVersion = 0;
} 