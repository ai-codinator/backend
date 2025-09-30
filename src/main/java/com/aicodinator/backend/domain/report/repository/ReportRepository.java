package com.aicodinator.backend.domain.report.repository;

import com.aicodinator.backend.domain.report.domain.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    
    // 사용자별 리포트 조회
    List<Report> findByUser_Id(Long userId);
    
    // 사용자별 리포트 최신순 조회
    List<Report> findByUser_IdOrderByCreatedAtDesc(Long userId);
    
    // 상태별 리포트 조회
    List<Report> findByStatus(Report.ReportStatus status);
    
    // 상태와 생성시간 기준 조회 (타임아웃 처리용)
    List<Report> findByStatusAndCreatedAtBefore(Report.ReportStatus status, LocalDateTime cutoffTime);
    
    // 지역별 리포트 조회
    List<Report> findByRegion_Id(Long regionId);
    
    // 완료된 리포트 중 특정 기간 내 조회
    List<Report> findByStatusAndCompletedAtBetween(Report.ReportStatus status, 
                                                    LocalDateTime start, 
                                                    LocalDateTime end);
}