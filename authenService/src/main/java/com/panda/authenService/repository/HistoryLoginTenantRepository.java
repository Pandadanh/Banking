package com.panda.authenService.repository;

import com.panda.authenService.entity.HistoryLoginTenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HistoryLoginTenantRepository extends JpaRepository<HistoryLoginTenant, UUID> {
    
    Optional<HistoryLoginTenant> findByGlobalUserIdAndTenantIdAndIsDeletedFalse(UUID globalUserId, UUID tenantId);
    
    List<HistoryLoginTenant> findByGlobalUserIdAndIsDeletedFalse(UUID globalUserId);
    
    List<HistoryLoginTenant> findByTenantIdAndIsDeletedFalse(UUID tenantId);
    
    @Query("SELECT h FROM HistoryLoginTenant h WHERE h.globalUserId = :userId AND h.tenantId = :tenantId " +
           "AND h.accessToken IS NOT NULL AND h.isDeleted = false " +
           "ORDER BY h.creationTime DESC")
    Optional<HistoryLoginTenant> findLatestValidTokenByUserAndTenant(@Param("userId") UUID userId, 
                                                                     @Param("tenantId") UUID tenantId);
    
    @Query("SELECT h FROM HistoryLoginTenant h WHERE h.accessToken = :accessToken AND h.isDeleted = false")
    Optional<HistoryLoginTenant> findByAccessToken(@Param("accessToken") String accessToken);
    
    @Query("SELECT h FROM HistoryLoginTenant h WHERE h.refreshToken = :refreshToken AND h.isDeleted = false")
    Optional<HistoryLoginTenant> findByRefreshToken(@Param("refreshToken") String refreshToken);
    
    @Query("SELECT h FROM HistoryLoginTenant h WHERE h.globalUserId = :userId " +
           "AND h.creationTime >= :fromDate AND h.isDeleted = false " +
           "ORDER BY h.creationTime DESC")
    List<HistoryLoginTenant> findRecentLoginsByUser(@Param("userId") UUID userId, 
                                                    @Param("fromDate") LocalDateTime fromDate);
}
