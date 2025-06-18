package com.panda.authenService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "HistoryLoginTenants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryLoginTenant {
    
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "Id", columnDefinition = "RAW(16)")
    private UUID id;
    
    @Column(name = "TenantId", columnDefinition = "RAW(16)")
    private UUID tenantId;
    
    @Column(name = "GlobalUserId", columnDefinition = "RAW(16)")
    private UUID globalUserId;
    
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
    
    @Column(name = "AccessToken", length = 1000)
    private String accessToken;
    
    @Column(name = "RefreshToken", length = 1000)
    private String refreshToken;
    
    @Column(name = "UserName", length = 255)
    private String userName;
    
    @PrePersist
    protected void onCreate() {
        if (creationTime == null) {
            creationTime = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        lastModificationTime = LocalDateTime.now();
    }
}
