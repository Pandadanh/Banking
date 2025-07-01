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
@Table("CARDS")
public class Card {
    @Id
    @Column("Id")
    private UUID id;

    @Column("CardNumber")
    private String cardNumber;

    @Column("AccountId")
    private UUID accountId;

    @Column("IssueDate")
    private LocalDateTime issueDate;

    @Column("ExpiryDate")
    private LocalDateTime expiryDate;

    @Column("Type")
    private String type;

    @Column("Status")
    private String status;

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