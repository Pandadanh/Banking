package com.panda.authenService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private UUID id;
    
    @Column(name = "TenantId", columnDefinition = "RAW(16)")
    private UUID tenantId;
    
    @Column(name = "UserName", length = 256, nullable = false)
    private String userName;
    
    @Column(name = "NormalizedUserName", length = 256, nullable = false)
    private String normalizedUserName;
    
    @Column(name = "Name", length = 64)
    private String name;
    
    @Column(name = "Surname", length = 64)
    private String surname;
    
    @Column(name = "Email", length = 256, nullable = false)
    private String email;
    
    @Column(name = "NormalizedEmail", length = 256, nullable = false)
    private String normalizedEmail;
    
    @Column(name = "EmailConfirmed", nullable = false)
    private Boolean emailConfirmed = false;
    
    @Column(name = "PasswordHash", length = 256)
    private String passwordHash;
    
    @Column(name = "SecurityStamp", length = 256, nullable = false)
    private String securityStamp;
    
    @Column(name = "IsExternal", nullable = false)
    private Boolean isExternal = false;
    
    @Column(name = "PhoneNumber", length = 16)
    private String phoneNumber;
    
    @Column(name = "PhoneNumberConfirmed", nullable = false)
    private Boolean phoneNumberConfirmed = false;
    
    @Column(name = "IsActive", nullable = false)
    private Boolean isActive = true;
    
    @Column(name = "TwoFactorEnabled", nullable = false)
    private Boolean twoFactorEnabled = false;
    
    @Column(name = "LockoutEnd")
    private OffsetDateTime lockoutEnd;
    
    @Column(name = "LockoutEnabled", nullable = false)
    private Boolean lockoutEnabled = false;
    
    @Column(name = "AccessFailedCount", nullable = false)
    private Integer accessFailedCount = 0;
    
    @Lob
    @Column(name = "ExtraProperties")
    private String extraProperties = "";
    
    @Column(name = "ConcurrencyStamp", length = 40, nullable = false)
    private String concurrencyStamp = "";
    
    @Column(name = "CreationTime", nullable = false)
    private LocalDateTime creationTime;
    
    @Column(name = "CreatorId", columnDefinition = "RAW(16)")
    private UUID creatorId;
    
    @Column(name = "LastModificationTime")
    private LocalDateTime lastModificationTime;
    
    @Column(name = "LastModifierId", columnDefinition = "RAW(16)")
    private UUID lastModifierId;
    
    @Column(name = "IsDeleted", nullable = false)
    private Boolean isDeleted = false;
    
    @Column(name = "DeleterId", columnDefinition = "RAW(16)")
    private UUID deleterId;
    
    @Column(name = "DeletionTime")
    private LocalDateTime deletionTime;
    
    @Column(name = "EntityVersion", nullable = false)
    private Integer entityVersion = 0;
    
    @Column(name = "LastPasswordChangeTime")
    private OffsetDateTime lastPasswordChangeTime;
    
    @Column(name = "ShouldChangePasswordOnNextLogin", nullable = false)
    private Boolean shouldChangePasswordOnNextLogin = false;
    
    @PrePersist
    protected void onCreate() {
        if (creationTime == null) {
            creationTime = LocalDateTime.now();
        }
        if (securityStamp == null) {
            securityStamp = "SECURITY_STAMP_" + UUID.randomUUID().toString();
        }
        if (concurrencyStamp == null || concurrencyStamp.isEmpty()) {
            concurrencyStamp = UUID.randomUUID().toString();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        lastModificationTime = LocalDateTime.now();
    }
}
