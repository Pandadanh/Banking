-- Insert sample data for testing
-- Connect as BANKING_USER

ALTER SESSION SET CURRENT_SCHEMA = BANKING_USER;

-- Insert sample tenant
DECLARE
    tenant_id RAW(16) := SYS_GUID();
    user_id RAW(16) := SYS_GUID();
    employee_id RAW(16) := SYS_GUID();
BEGIN
    -- Insert sample user (password is 'password123' hashed with BCrypt)
    INSERT INTO Users (
        Id, TenantId, UserName, NormalizedUserName, Name, Surname, 
        Email, NormalizedEmail, EmailConfirmed, 
        PasswordHash, SecurityStamp, IsExternal, PhoneNumber, 
        PhoneNumberConfirmed, IsActive, TwoFactorEnabled, 
        LockoutEnabled, AccessFailedCount, CreationTime, 
        EntityVersion, ShouldChangePasswordOnNextLogin
    ) VALUES (
        user_id, tenant_id, 'testuser', 'TESTUSER', 'Test', 'User',
        'testuser@example.com', 'TESTUSER@EXAMPLE.COM', 1,
        '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password123
        'SECURITY_STAMP_' || RAWTOHEX(SYS_GUID()), 0, '0123456789',
        1, 1, 0,
        0, 0, SYSTIMESTAMP,
        0, 0
    );

    -- Insert sample employee
    INSERT INTO Employee (
        Id, TenantId, Code, ERPCode, FirstName, LastName,
        Email, Phone, Active, EffectiveDate, IdentityUserId,
        CreationTime, EmployeeType, UserId, UserCode
    ) VALUES (
        employee_id, tenant_id, 'EMP001', 'ERP001', 'Test', 'Employee',
        'testuser@example.com', '0123456789', 1, SYSTIMESTAMP, user_id,
        SYSTIMESTAMP, 1, user_id, 'USR001'
    );

    -- Insert another user for testing
    INSERT INTO Users (
        Id, TenantId, UserName, NormalizedUserName, Name, Surname, 
        Email, NormalizedEmail, EmailConfirmed, 
        PasswordHash, SecurityStamp, IsExternal, PhoneNumber, 
        PhoneNumberConfirmed, IsActive, TwoFactorEnabled, 
        LockoutEnabled, AccessFailedCount, CreationTime, 
        EntityVersion, ShouldChangePasswordOnNextLogin
    ) VALUES (
        SYS_GUID(), tenant_id, 'admin', 'ADMIN', 'Admin', 'User',
        'admin@example.com', 'ADMIN@EXAMPLE.COM', 1,
        '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password123
        'SECURITY_STAMP_' || RAWTOHEX(SYS_GUID()), 0, '0987654321',
        1, 1, 0,
        0, 0, SYSTIMESTAMP,
        0, 0
    );

    COMMIT;
END;
/

-- Create sequences for auto-increment if needed
CREATE SEQUENCE user_seq START WITH 1000 INCREMENT BY 1;
CREATE SEQUENCE employee_seq START WITH 1000 INCREMENT BY 1;
CREATE SEQUENCE login_transaction_seq START WITH 1000 INCREMENT BY 1;

COMMIT;
