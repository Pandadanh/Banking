package com.panda.authenService.repository;

import com.panda.authenService.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    
    Optional<Employee> findByUserIdAndIsDeletedFalse(UUID userId);
    
    Optional<Employee> findByIdentityUserIdAndIsDeletedFalse(UUID identityUserId);
    
    Optional<Employee> findByCodeAndIsDeletedFalse(String code);
    
    Optional<Employee> findByUserCodeAndIsDeletedFalse(String userCode);
    
    @Query("SELECT e FROM Employee e WHERE e.userId = :userId AND e.active = true AND e.isDeleted = false")
    Optional<Employee> findActiveEmployeeByUserId(@Param("userId") UUID userId);
    
    @Query("SELECT e FROM Employee e WHERE e.email = :email AND e.active = true AND e.isDeleted = false")
    Optional<Employee> findActiveEmployeeByEmail(@Param("email") String email);
}
