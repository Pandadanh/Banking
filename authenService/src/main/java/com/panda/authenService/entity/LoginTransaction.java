package com.panda.authenService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "LoginTransactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginTransaction {
    
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "Id", columnDefinition = "RAW(16)")
    private UUID id;
    
    @Column(name = "TenantId", columnDefinition = "RAW(16)")
    private UUID tenantId;
    
    @Column(name = "UserId", columnDefinition = "RAW(16)")
    private UUID userId;
    
    @Column(name = "EmployeeId", columnDefinition = "RAW(16)")
    private UUID employeeId;
    
    @Column(name = "LoginStatus", length = 3)
    private String loginStatus; // "SUC" for success, "FAI" for failure
    
    @Column(name = "Error", length = 255)
    private String error;
    
    @Column(name = "ConcurrencyStamp", length = 40)
    private String concurrencyStamp;
    
    @Column(name = "CreationTime", nullable = false)
    private LocalDateTime creationTime;
    
    @Column(name = "CreatorId", columnDefinition = "RAW(16)")
    private UUID creatorId;
    
    @Column(name = "LastModificationTime")
    private LocalDateTime lastModificationTime;
    
    @Column(name = "LastModifierId", columnDefinition = "RAW(16)")
    private UUID lastModifierId;
    
    @Column(name = "IsDeleted")
    private Boolean isDeleted = false;
    
    @Column(name = "DeleterId", columnDefinition = "RAW(16)")
    private UUID deleterId;
    
    @Column(name = "DeletionTime")
    private LocalDateTime deletionTime;
    
    @Column(name = "TenantCode", length = 50)
    private String tenantCode;
    
    @Column(name = "UserName", length = 255)
    private String userName;
    
    @PrePersist
    protected void onCreate() {
        if (creationTime == null) {
            creationTime = LocalDateTime.now();
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
