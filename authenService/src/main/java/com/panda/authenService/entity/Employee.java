package com.panda.authenService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "Id", columnDefinition = "RAW(16)")
    private UUID id;
    
    @Column(name = "TenantId", columnDefinition = "RAW(16)")
    private UUID tenantId;
    
    @Column(name = "Code", length = 20)
    private String code;
    
    @Column(name = "ERPCode", length = 20)
    private String erpCode;
    
    @Column(name = "FirstName", length = 255)
    private String firstName;
    
    @Column(name = "LastName", length = 255)
    private String lastName;
    
    @Column(name = "DateOfBirth")
    private LocalDateTime dateOfBirth;
    
    @Column(name = "IdCardNumber", length = 255)
    private String idCardNumber;
    
    @Column(name = "Email", length = 255)
    private String email;
    
    @Column(name = "Phone", length = 255)
    private String phone;
    
    @Column(name = "Address", length = 500)
    private String address;
    
    @Column(name = "Active")
    private Boolean active;
    
    @Column(name = "EffectiveDate")
    private LocalDateTime effectiveDate;
    
    @Column(name = "EndDate")
    private LocalDateTime endDate;
    
    @Column(name = "IdentityUserId", columnDefinition = "RAW(16)")
    private UUID identityUserId;
    
    @Column(name = "WorkingPositionId", columnDefinition = "RAW(16)")
    private UUID workingPositionId;
    
    @Lob
    @Column(name = "ExtraProperties")
    private String extraProperties;
    
    @Column(name = "ConcurrencyStamp", length = 40)
    private String concurrencyStamp;
    
    @Column(name = "CreationTime")
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
    
    @Column(name = "EmployeeType")
    private Integer employeeType;
    
    @Column(name = "UserId", columnDefinition = "RAW(16)")
    private UUID userId;
    
    @Column(name = "RouteId", columnDefinition = "RAW(16)")
    private UUID routeId;
    
    @Column(name = "UserCode", length = 50)
    private String userCode;
    
    @Column(name = "SalesmanTypeId", columnDefinition = "RAW(16)")
    private UUID salesmanTypeId;
    
    @Column(name = "SalesmanTypeCode", length = 50)
    private String salesmanTypeCode;
    
    @Lob
    @Column(name = "Type")
    private String type;
    
    @Column(name = "FileId", columnDefinition = "RAW(16)")
    private UUID fileId;
    
    @Column(name = "FileName", length = 1000)
    private String fileName;
    
    @Column(name = "FilePath", length = 1000)
    private String filePath;
    
    @Column(name = "FileUrl", length = 1000)
    private String fileUrl;
    
    @Column(name = "Gender")
    private Integer gender;
    
    @Column(name = "TaxCode", length = 50)
    private String taxCode;
    
    @Column(name = "Birthday")
    private LocalDateTime birthday;
    
    @Lob
    @Column(name = "CompanyCode")
    private String companyCode;
    
    @Column(name = "CompanyId", columnDefinition = "RAW(16)")
    private UUID companyId;
    
    @Lob
    @Column(name = "CompanyName")
    private String companyName;
    
    @Lob
    @Column(name = "Fax")
    private String fax;
    
    @Lob
    @Column(name = "HrmsCode")
    private String hrmsCode;
    
    @Column(name = "IsDeliveryMan")
    private Boolean isDeliveryMan;
    
    @Column(name = "IsUsed")
    private Boolean isUsed;
    
    @Lob
    @Column(name = "Remark")
    private String remark;
    
    @Lob
    @Column(name = "SalesmanType")
    private String salesmanType;
    
    @Lob
    @Column(name = "SalesmanTypeName")
    private String salesmanTypeName;
    
    @Column(name = "SlpCode")
    private Integer slpCode;
    
    @Lob
    @Column(name = "TaxId")
    private String taxId;
    
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
