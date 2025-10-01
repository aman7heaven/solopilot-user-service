package com.solopilot.user.repository;

import com.solopilot.user.entity.common.PortfolioVisitorAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
public interface PortfolioVisitorAnalyticsRepository extends JpaRepository<PortfolioVisitorAnalytics, Long> {

    /**
     * Check if a session already exists in analytics
     */
    boolean existsBySessionId(String sessionId);

    /**
     * Find analytics record by session ID
     */
    Optional<PortfolioVisitorAnalytics> findBySessionId(String sessionId);

    /**
     * Count distinct visitors since a given timestamp
     */
    @Query("SELECT COUNT(DISTINCT pa.sessionId) " +
            "FROM PortfolioVisitorAnalytics pa " +
            "WHERE pa.lastVisitedTimestamp >= :since")
    long countVisitorsSince(OffsetDateTime since);

    /**
     * Count visitors by device type
     */
    @Query("SELECT COUNT(pa) FROM PortfolioVisitorAnalytics pa WHERE pa.deviceType = :deviceType")
    long countByDeviceType(String deviceType);

    /**
     * Count new visitors
     */
    @Query("SELECT COUNT(DISTINCT pa.sessionId) FROM PortfolioVisitorAnalytics pa WHERE pa.isNewVisitor = true")
    long countNewVisitors();

    /**
     * Count returning visitors
     */
    @Query("SELECT COUNT(DISTINCT pa.sessionId) FROM PortfolioVisitorAnalytics pa WHERE pa.isNewVisitor = false")
    long countReturningVisitors();
}
