package com.panda.mdmService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("USERS")
public class User {
    @Id
    @Column("Id")
    private UUID id;

    @Column("TenantId")
    private UUID tenantId;

    @Column("UserName")
    private String userName;

    @Column("NormalizedUserName")
    private String normalizedUserName;

    @Column("Name")
    private String name;

    @Column("Surname")
    private String surname;

    @Column("Email")
    private String email;

    @Column("NormalizedEmail")
    private String normalizedEmail;

    @Column("EmailConfirmed")
    private Boolean emailConfirmed = false;

    @Column("PasswordHash")
    private String passwordHash;

    @Column("SecurityStamp")
    private String securityStamp;

    @Column("IsExternal")
    private Boolean isExternal = false;

    @Column("PhoneNumber")
    private String phoneNumber;

    @Column("PhoneNumberConfirmed")
    private Boolean phoneNumberConfirmed = false;

    @Column("IsActive")
    private Boolean isActive = true;

    @Column("TwoFactorEnabled")
    private Boolean twoFactorEnabled = false;

    @Column("LockoutEnd")
    private OffsetDateTime lockoutEnd;

    @Column("LockoutEnabled")
    private Boolean lockoutEnabled = false;

    @Column("AccessFailedCount")
    private Integer accessFailedCount = 0;

    @Column("ExtraProperties")
    private String extraProperties = "";

    @Column("ConcurrencyStamp")
    private String concurrencyStamp = "";

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

    @Column("LastPasswordChangeTime")
    private OffsetDateTime lastPasswordChangeTime;

    @Column("ShouldChangePasswordOnNextLogin")
    private Boolean shouldChangePasswordOnNextLogin = false;
}