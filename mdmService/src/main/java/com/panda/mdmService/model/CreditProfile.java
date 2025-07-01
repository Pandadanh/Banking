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
@Table("CREDIT_PROFILES")
public class CreditProfile {
    @Id
    @Column("Id")
    private UUID id;

    @Column("CustomerId")
    private UUID customerId;

    @Column("CreditScore")
    private BigDecimal creditScore;

    @Column("RiskLevel")
    private String riskLevel;

    @Column("Note")
    private String note;
} 