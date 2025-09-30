package com.aicodinator.backend.domain.report.controller;

import com.aicodinator.backend.domain.report.domain.entity.Report;
import com.aicodinator.backend.domain.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Report", description = "AI 분석 리포트 API")
public class ReportController {
    
    private final ReportService reportService;
    
    @GetMapping("/{id}")
    @Operation(summary = "리포트 단건 조회", description = "ID로 리포트를 조회합니다")
    public ResponseEntity<Report> getReport(@PathVariable Long id) {
        return reportService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "사용자별 리포트 목록", description = "특정 사용자의 모든 리포트를 조회합니다")
    public ResponseEntity<List<Report>> getUserReports(@PathVariable Long userId) {
        List<Report> reports = reportService.findByUserId(userId);
        return ResponseEntity.ok(reports);
    }
    
    @GetMapping("/user/{userId}/latest")
    @Operation(summary = "사용자 최신 리포트", description = "특정 사용자의 가장 최근 리포트를 조회합니다")
    public ResponseEntity<Report> getUserLatestReport(@PathVariable Long userId) {
        return reportService.findLatestByUserId(userId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "상태별 리포트 목록", description = "특정 상태의 리포트를 모두 조회합니다")
    public ResponseEntity<List<Report>> getReportsByStatus(@PathVariable Report.ReportStatus status) {
        List<Report> reports = reportService.findByStatus(status);
        return ResponseEntity.ok(reports);
    }
    
    @GetMapping("/processing")
    @Operation(summary = "처리중인 리포트 목록", description = "현재 처리중인 모든 리포트를 조회합니다")
    public ResponseEntity<List<Report>> getProcessingReports() {
        List<Report> reports = reportService.findProcessingReports();
        return ResponseEntity.ok(reports);
    }
    
    // ===== AI 리포트 생성 프로세스 =====
    
    @PostMapping("/create")
    @Operation(summary = "리포트 생성 시작", description = "새로운 AI 분석 리포트 생성을 시작합니다")
    public ResponseEntity<Report> createReport(
            @Parameter(description = "사용자 ID") @RequestParam Long userId,
            @Parameter(description = "지역 ID") @RequestParam Long regionId,
            @Parameter(description = "셀프체크 ID") @RequestParam(required = false) Long selfCheckId) {
        
        Report report = reportService.createReport(userId, regionId, selfCheckId);
        log.info("Created report {} for user {}", report.getId(), userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(report);
    }
    
    @PutMapping("/{id}/start-processing")
    @Operation(summary = "리포트 처리 시작", description = "리포트 처리를 시작합니다")
    public ResponseEntity<Void> startProcessing(@PathVariable Long id) {
        reportService.startProcessing(id);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{id}/analysis")
    @Operation(summary = "분석 결과 저장", description = "AI 분석 결과를 리포트에 저장합니다")
    public ResponseEntity<Void> updateAnalysis(
            @PathVariable Long id,
            @RequestBody Map<String, Object> analysisData) {
        
        BigDecimal prdiAmount = new BigDecimal(analysisData.get("prdiAmount").toString());
        List<String> recommendedRegions = (List<String>) analysisData.get("recommendedRegions");
        Map<String, Object> careerAnalysis = (Map<String, Object>) analysisData.get("careerAnalysis");
        Map<String, Object> housingAnalysis = (Map<String, Object>) analysisData.get("housingAnalysis");
        Map<String, Object> policyAnalysis = (Map<String, Object>) analysisData.get("policyAnalysis");
        Map<String, Object> lifestyleAnalysis = (Map<String, Object>) analysisData.get("lifestyleAnalysis");
        
        reportService.updateAnalysisResult(id, prdiAmount, recommendedRegions,
                                          careerAnalysis, housingAnalysis,
                                          policyAnalysis, lifestyleAnalysis);
        
        log.info("Updated analysis for report {}", id);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{id}/scenarios")
    @Operation(summary = "시나리오 저장", description = "일일/월간/연간 시나리오를 저장합니다")
    public ResponseEntity<Void> updateScenarios(
            @PathVariable Long id,
            @RequestBody Map<String, String> scenarios) {
        
        String dailyScenario = scenarios.get("dailyScenario");
        String monthlyScenario = scenarios.get("monthlyScenario");
        String yearlyScenario = scenarios.get("yearlyScenario");
        
        reportService.updateScenarios(id, dailyScenario, monthlyScenario, yearlyScenario);
        log.info("Updated scenarios for report {}", id);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{id}/prdi-details")
    @Operation(summary = "PRDI 상세 저장", description = "PRDI 계산 상세 정보를 저장합니다")
    public ResponseEntity<Void> updatePrdiDetails(
            @PathVariable Long id,
            @RequestBody Map<String, BigDecimal> prdiDetails) {
        
        BigDecimal expectedIncome = prdiDetails.get("expectedIncome");
        BigDecimal totalSubsidy = prdiDetails.get("totalSubsidy");
        BigDecimal housingCost = prdiDetails.get("housingCost");
        BigDecimal livingCost = prdiDetails.get("livingCost");
        BigDecimal taxAmount = prdiDetails.get("taxAmount");
        
        reportService.updatePrdiDetails(id, expectedIncome, totalSubsidy,
                                        housingCost, livingCost, taxAmount);
        
        log.info("Updated PRDI details for report {}", id);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{id}/recommendations")
    @Operation(summary = "추천 목록 저장", description = "AI가 추천한 기업/주거/정책 ID 목록을 저장합니다")
    public ResponseEntity<Void> updateRecommendations(
            @PathVariable Long id,
            @RequestBody Map<String, List<?>> recommendations) {
        
        List<Long> companyIds = (List<Long>) recommendations.get("companyIds");
        List<Long> housingIds = (List<Long>) recommendations.get("housingIds");
        List<String> policyIds = (List<String>) recommendations.get("policyIds");
        List<String> subsidyIds = (List<String>) recommendations.get("subsidyIds");
        
        reportService.updateRecommendations(id, companyIds, housingIds, policyIds, subsidyIds);
        log.info("Updated recommendations for report {}", id);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{id}/fail")
    @Operation(summary = "리포트 실패 처리", description = "리포트 생성 실패를 기록합니다")
    public ResponseEntity<Void> failReport(
            @PathVariable Long id,
            @RequestParam String errorMessage) {
        
        reportService.failReport(id, errorMessage);
        log.error("Report {} failed: {}", id, errorMessage);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/timeout-check")
    @Operation(summary = "타임아웃 체크", description = "오래된 처리중 리포트를 타임아웃 처리합니다")
    public ResponseEntity<Void> checkTimeouts(
            @RequestParam(defaultValue = "30") int timeoutMinutes) {
        
        reportService.timeoutOldProcessingReports(timeoutMinutes);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "리포트 삭제", description = "리포트를 삭제합니다")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        log.info("Deleted report {}", id);
        return ResponseEntity.noContent().build();
    }
}
