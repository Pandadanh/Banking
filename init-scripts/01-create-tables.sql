-- Create Banking Authentication Database Schema
-- Connect as SYSTEM user

-- Create tablespace for banking data
CREATE TABLESPACE BANKING_DATA
DATAFILE '/opt/oracle/oradata/XE/banking_data01.dbf' 
SIZE 100M AUTOEXTEND ON NEXT 10M MAXSIZE 1G;

-- Create banking user
CREATE USER BANKING_USER IDENTIFIED BY BankingPassword123
DEFAULT TABLESPACE BANKING_DATA
TEMPORARY TABLESPACE TEMP;

-- Grant privileges
GRANT CONNECT, RESOURCE, CREATE VIEW, CREATE SEQUENCE TO BANKING_USER;
GRANT UNLIMITED TABLESPACE TO BANKING_USER;

-- Connect as BANKING_USER
ALTER SESSION SET CURRENT_SCHEMA = BANKING_USER;

-- Create Users table
CREATE TABLE Users (
    Id RAW(16) DEFAULT SYS_GUID() NOT NULL,
    TenantId RAW(16) NULL,
    UserName NVARCHAR2(256) NOT NULL,
    NormalizedUserName NVARCHAR2(256) NOT NULL,
    Name NVARCHAR2(64) NULL,
    Surname NVARCHAR2(64) NULL,
    Email NVARCHAR2(256) NOT NULL,
    NormalizedEmail NVARCHAR2(256) NOT NULL,
    EmailConfirmed NUMBER(1) DEFAULT 0 NOT NULL,
    PasswordHash NVARCHAR2(256) NULL,
    SecurityStamp NVARCHAR2(256) NOT NULL,
    IsExternal NUMBER(1) DEFAULT 0 NOT NULL,
    PhoneNumber NVARCHAR2(16) NULL,
    PhoneNumberConfirmed NUMBER(1) DEFAULT 0 NOT NULL,
    IsActive NUMBER(1) NOT NULL,
    TwoFactorEnabled NUMBER(1) DEFAULT 0 NOT NULL,
    LockoutEnd TIMESTAMP WITH TIME ZONE NULL,
    LockoutEnabled NUMBER(1) DEFAULT 0 NOT NULL,
    AccessFailedCount NUMBER DEFAULT 0 NOT NULL,
    ExtraProperties CLOB DEFAULT EMPTY_CLOB() NOT NULL,
    ConcurrencyStamp NVARCHAR2(40) DEFAULT '' NOT NULL,
    CreationTime TIMESTAMP(7) NOT NULL,
    CreatorId RAW(16) NULL,
    LastModificationTime TIMESTAMP(7) NULL,
    LastModifierId RAW(16) NULL,
    IsDeleted NUMBER(1) DEFAULT 0 NOT NULL,
    DeleterId RAW(16) NULL,
    DeletionTime TIMESTAMP(7) NULL,
    EntityVersion NUMBER DEFAULT 0 NOT NULL,
    LastPasswordChangeTime TIMESTAMP WITH TIME ZONE NULL,
    ShouldChangePasswordOnNextLogin NUMBER(1) DEFAULT 0 NOT NULL,
    CONSTRAINT PK_Users PRIMARY KEY (Id)
);

-- Create Employee table
CREATE TABLE Employee (
    Id RAW(16) DEFAULT SYS_GUID() NOT NULL,
    TenantId RAW(16) NULL,
    Code NVARCHAR2(20) NULL,
    ERPCode NVARCHAR2(20) NULL,
    FirstName NVARCHAR2(255) NULL,
    LastName NVARCHAR2(255) NULL,
    DateOfBirth TIMESTAMP(7) NULL,
    IdCardNumber NVARCHAR2(255) NULL,
    Email NVARCHAR2(255) NULL,
    Phone NVARCHAR2(255) NULL,
    Address NVARCHAR2(500) NULL,
    Active NUMBER(1) NULL,
    EffectiveDate TIMESTAMP(7) NULL,
    EndDate TIMESTAMP(7) NULL,
    IdentityUserId RAW(16) NULL,
    WorkingPositionId RAW(16) NULL,
    ExtraProperties CLOB NULL,
    ConcurrencyStamp NVARCHAR2(40) NULL,
    CreationTime TIMESTAMP(7) NULL,
    CreatorId RAW(16) NULL,
    LastModificationTime TIMESTAMP(7) NULL,
    LastModifierId RAW(16) NULL,
    IsDeleted NUMBER(1) DEFAULT 0 NULL,
    DeleterId RAW(16) NULL,
    DeletionTime TIMESTAMP(7) NULL,
    EmployeeType NUMBER NULL,
    UserId RAW(16) NULL,
    RouteId RAW(16) NULL,
    UserCode NVARCHAR2(50) NULL,
    SalesmanTypeId RAW(16) NULL,
    SalesmanTypeCode NVARCHAR2(50) NULL,
    Type CLOB NULL,
    FileId RAW(16) NULL,
    FileName NVARCHAR2(1000) NULL,
    FilePath NVARCHAR2(1000) NULL,
    FileUrl NVARCHAR2(1000) NULL,
    Gender NUMBER NULL,
    TaxCode NVARCHAR2(50) NULL,
    Birthday TIMESTAMP(7) NULL,
    CompanyCode CLOB NULL,
    CompanyId RAW(16) NULL,
    CompanyName CLOB NULL,
    Fax CLOB NULL,
    HrmsCode CLOB NULL,
    IsDeliveryMan NUMBER(1) NULL,
    IsUsed NUMBER(1) NULL,
    Remark CLOB NULL,
    SalesmanType CLOB NULL,
    SalesmanTypeName CLOB NULL,
    SlpCode NUMBER NULL,
    TaxId CLOB NULL,
    CONSTRAINT PK_Employee PRIMARY KEY (Id)
);

-- Create HistoryLoginTenants table
CREATE TABLE HistoryLoginTenants (
    Id RAW(16) DEFAULT SYS_GUID() NOT NULL,
    TenantId RAW(16) NULL,
    GlobalUserId RAW(16) NULL,
    CreationTime TIMESTAMP(7) NOT NULL,
    CreatorId RAW(16) NULL,
    LastModificationTime TIMESTAMP(7) NULL,
    LastModifierId RAW(16) NULL,
    IsDeleted NUMBER(1) DEFAULT 0 NULL,
    DeleterId RAW(16) NULL,
    DeletionTime TIMESTAMP(7) NULL,
    AccessToken NVARCHAR2(1000) NULL,
    RefreshToken NVARCHAR2(1000) NULL,
    UserName NVARCHAR2(255) NULL,
    CONSTRAINT PK_HistoryLoginTenants PRIMARY KEY (Id)
);

-- Create LoginTransactions table
CREATE TABLE LoginTransactions (
    Id RAW(16) DEFAULT SYS_GUID() NOT NULL,
    TenantId RAW(16) NULL,
    UserId RAW(16) NULL,
    EmployeeId RAW(16) NULL,
    LoginStatus NVARCHAR2(3) NULL,
    Error NVARCHAR2(255) NULL,
    ConcurrencyStamp NVARCHAR2(40) NULL,
    CreationTime TIMESTAMP(7) NOT NULL,
    CreatorId RAW(16) NULL,
    LastModificationTime TIMESTAMP(7) NULL,
    LastModifierId RAW(16) NULL,
    IsDeleted NUMBER(1) DEFAULT 0 NULL,
    DeleterId RAW(16) NULL,
    DeletionTime TIMESTAMP(7) NULL,
    TenantCode NVARCHAR2(50) NULL,
    UserName NVARCHAR2(255) NULL,
    CONSTRAINT PK_LoginTransactions PRIMARY KEY (Id)
);

-- Create indexes for better performance
CREATE INDEX IX_Users_Email ON Users (Email);
CREATE INDEX IX_Users_NormalizedEmail ON Users (NormalizedEmail);
CREATE INDEX IX_Users_NormalizedUserName ON Users (NormalizedUserName);
CREATE INDEX IX_Users_UserName ON Users (UserName);
CREATE INDEX IX_HistoryLoginTenants_UserId ON HistoryLoginTenants (GlobalUserId);
CREATE INDEX IX_HistoryLoginTenants_TenantId ON HistoryLoginTenants (TenantId);
CREATE INDEX IX_LoginTransactions_UserId ON LoginTransactions (UserId);
CREATE INDEX IX_LoginTransactions_TenantId ON LoginTransactions (TenantId);

COMMIT;
