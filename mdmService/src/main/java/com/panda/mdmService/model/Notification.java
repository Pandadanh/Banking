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
@Table("NOTIFICATIONS")
public class Notification {
    @Id
    @Column("Id")
    private UUID id;

    @Column("UserId")
    private UUID userId;

    @Column("Message")
    private String message;

    @Column("SentTime")
    private LocalDateTime sentTime;

    @Column("Status")
    private String status;
} 