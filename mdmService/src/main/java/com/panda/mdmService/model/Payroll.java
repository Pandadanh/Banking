package com.panda.mdmService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("PAYROLLS")
public class Payroll {
    @Id
    @Column("Id")
    private UUID id;

    @Column("EmployeeId")
    private UUID employeeId;

    @Column("PayDate")
    private LocalDateTime payDate;

    @Column("Amount")
    private BigDecimal amount;

    @Column("Note")
    private String note;
} 