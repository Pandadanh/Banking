package com.panda.authenService.repository;

import com.panda.authenService.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    
    Optional<User> findByUserNameAndIsDeletedFalse(String userName);
    
    Optional<User> findByNormalizedUserNameAndIsDeletedFalse(String normalizedUserName);
    
    Optional<User> findByEmailAndIsDeletedFalse(String email);
    
    Optional<User> findByNormalizedEmailAndIsDeletedFalse(String normalizedEmail);
    
    boolean existsByUserNameAndIsDeletedFalse(String userName);
    
    boolean existsByEmailAndIsDeletedFalse(String email);
    
    @Query("SELECT u FROM User u WHERE u.userName = :userName AND u.isActive = true AND u.isDeleted = false")
    Optional<User> findActiveUserByUserName(@Param("userName") String userName);
    
    @Query("SELECT u FROM User u WHERE u.tenantId = :tenantId AND u.isActive = true AND u.isDeleted = false")
    Optional<User> findActiveUserByTenantId(@Param("tenantId") UUID tenantId);
}
