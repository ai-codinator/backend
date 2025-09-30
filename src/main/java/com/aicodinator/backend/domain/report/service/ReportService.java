package com.aicodinator.backend.domain.report.service;

import com.aicodinator.backend.domain.report.domain.entity.Report;
import com.aicodinator.backend.domain.report.repository.ReportRepository;
import com.aicodinator.backend.domain.user.domain.entity.User;
import com.aicodinator.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {
    
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    
    // 리포트 단건 조회
    public Optional<Report> findById(Long id) {
        return reportRepository.findById(id);
    }
    
    // 사용자별 리포트 조회
    public List<Report> findByUserId(Long userId) {
        return reportRepository.findByUser_Id(userId);
    }
    
    // 사용자의 최신 리포트 조회
    public Optional<Report> findLatestByUserId(Long userId) {
        List<Report> reports = reportRepository.findByUser_IdOrderByCreatedAtDesc(userId);
        return reports.isEmpty() ? Optional.empty() : Optional.of(reports.get(0));
    }
    
    // 상태별 리포트 조회
    public List<Report> findByStatus(Report.ReportStatus status) {
        return reportRepository.findByStatus(status);
    }
    
    // 처리중인 리포트 조회
    public List<Report> findProcessingReports() {
        return reportRepository.findByStatus(Report.ReportStatus.PROCESSING);
    }
    
    // AI 리포트 생성 시작
    @Transactional
    public Report createReport(Long userId, Long regionId, Long selfCheckId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        
        Report report = Report.builder()
            .user(user)
            .status(Report.ReportStatus.PENDING)
            .build();
        
        Report savedReport = reportRepository.save(report);
        log.info("Created new report for user {}: {}", userId, savedReport.getId());
        
        return savedReport;
    }
    
    // 리포트 처리 시작
    @Transactional
    public void startProcessing(Long reportId) {
        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new IllegalArgumentException("Report not found: " + reportId));
        
        report.startProcessing();
        reportRepository.save(report);
        log.info("Started processing report: {}", reportId);
    }
    
    // AI 분석 결과 저장
    @Transactional
    public void updateAnalysisResult(Long reportId,
                                     BigDecimal prdiAmount,
                                     List<String> recommendedRegions,
                                     Map<String, Object> careerAnalysis,
                                     Map<String, Object> housingAnalysis,
                                     Map<String, Object> policyAnalysis,
                                     Map<String, Object> lifestyleAnalysis) {
        
        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new IllegalArgumentException("Report not found: " + reportId));
        
        report.completeAnalysis(prdiAmount, recommendedRegions, 
                               careerAnalysis, housingAnalysis, 
                               policyAnalysis, lifestyleAnalysis);
        
        reportRepository.save(report);
        log.info("Updated analysis result for report: {}", reportId);
    }
    
    // 시나리오 업데이트
    @Transactional
    public void updateScenarios(Long reportId,
                                String dailyScenario,
                                String monthlyScenario,
                                String yearlyScenario) {
        
        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new IllegalArgumentException("Report not found: " + reportId));
        
        report.updateScenarios(dailyScenario, monthlyScenario, yearlyScenario);
        reportRepository.save(report);
        log.info("Updated scenarios for report: {}", reportId);
    }
    
    // PRDI 상세 정보 업데이트
    @Transactional
    public void updatePrdiDetails(Long reportId,
                                  BigDecimal expectedIncome,
                                  BigDecimal totalSubsidy,
                                  BigDecimal housingCost,
                                  BigDecimal livingCost,
                                  BigDecimal taxAmount) {
        
        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new IllegalArgumentException("Report not found: " + reportId));
        
        report.updatePrdiDetails(expectedIncome, totalSubsidy, 
                                housingCost, livingCost, taxAmount);
        reportRepository.save(report);
        log.info("Updated PRDI details for report: {}", reportId);
    }
    
    // 추천 목록 업데이트
    @Transactional
    public void updateRecommendations(Long reportId,
                                      List<Long> companyIds,
                                      List<Long> housingIds,
                                      List<String> policyIds,
                                      List<String> subsidyIds) {
        
        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new IllegalArgumentException("Report not found: " + reportId));
        
        report.updateRecommendations(companyIds, housingIds, policyIds, subsidyIds);
        reportRepository.save(report);
        log.info("Updated recommendations for report: {}", reportId);
    }
    
    // 리포트 실패 처리
    @Transactional
    public void failReport(Long reportId, String errorMessage) {
        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new IllegalArgumentException("Report not found: " + reportId));
        
        report.fail(errorMessage);
        reportRepository.save(report);
        log.error("Report {} failed: {}", reportId, errorMessage);
    }
    
    // 오래된 처리중 리포트 타임아웃 처리
    @Transactional
    public void timeoutOldProcessingReports(int timeoutMinutes) {
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(timeoutMinutes);
        List<Report> oldReports = reportRepository.findByStatusAndCreatedAtBefore(
            Report.ReportStatus.PROCESSING, cutoffTime
        );
        
        for (Report report : oldReports) {
            report.fail("Processing timeout after " + timeoutMinutes + " minutes");
            reportRepository.save(report);
            log.warn("Timed out report: {}", report.getId());
        }
    }
    
    // 리포트 삭제
    @Transactional
    public void deleteReport(Long reportId) {
        reportRepository.deleteById(reportId);
        log.info("Deleted report: {}", reportId);
    }
}