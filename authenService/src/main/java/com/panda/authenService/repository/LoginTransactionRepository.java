package com.panda.authenService.repository;

import com.panda.authenService.entity.LoginTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface LoginTransactionRepository extends JpaRepository<LoginTransaction, UUID> {
    
    List<LoginTransaction> findByUserIdAndIsDeletedFalse(UUID userId);
    
    List<LoginTransaction> findByTenantIdAndIsDeletedFalse(UUID tenantId);
    
    List<LoginTransaction> findByUserNameAndIsDeletedFalse(String userName);
    
    @Query("SELECT lt FROM LoginTransaction lt WHERE lt.userId = :userId " +
           "AND lt.creationTime >= :fromDate AND lt.isDeleted = false " +
           "ORDER BY lt.creationTime DESC")
    List<LoginTransaction> findRecentLoginsByUser(@Param("userId") UUID userId, 
                                                  @Param("fromDate") LocalDateTime fromDate);
    
    @Query("SELECT lt FROM LoginTransaction lt WHERE lt.userName = :userName " +
           "AND lt.loginStatus = 'FAI' AND lt.creationTime >= :fromDate " +
           "AND lt.isDeleted = false ORDER BY lt.creationTime DESC")
    List<LoginTransaction> findRecentFailedLoginsByUserName(@Param("userName") String userName, 
                                                            @Param("fromDate") LocalDateTime fromDate);
    
    @Query("SELECT COUNT(lt) FROM LoginTransaction lt WHERE lt.userName = :userName " +
           "AND lt.loginStatus = 'FAI' AND lt.creationTime >= :fromDate " +
           "AND lt.isDeleted = false")
    long countFailedLoginAttempts(@Param("userName") String userName, 
                                  @Param("fromDate") LocalDateTime fromDate);
    
    @Query("SELECT lt FROM LoginTransaction lt WHERE lt.loginStatus = :status " +
           "AND lt.creationTime >= :fromDate AND lt.isDeleted = false " +
           "ORDER BY lt.creationTime DESC")
    List<LoginTransaction> findLoginsByStatusSince(@Param("status") String status, 
                                                   @Param("fromDate") LocalDateTime fromDate);
}
