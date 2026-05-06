package com.portfolio.repository;

import com.portfolio.model.VisitorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitorLogRepository extends JpaRepository<VisitorLog, Long> {

    long countByVisitedAtAfter(LocalDateTime date);

    @Query("SELECT COUNT(DISTINCT v.ipAddress) FROM VisitorLog v")
    long countUniqueVisitors();

    @Query("SELECT COUNT(DISTINCT v.ipAddress) FROM VisitorLog v WHERE v.visitedAt > :date")
    long countUniqueVisitorsAfter(LocalDateTime date);

    List<VisitorLog> findTop20ByOrderByVisitedAtDesc();
}