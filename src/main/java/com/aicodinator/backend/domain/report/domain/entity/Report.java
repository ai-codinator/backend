package com.aicodinator.backend.domain.report.domain.entity;

import com.aicodinator.backend.domain.region.domain.entity.Region;
import com.aicodinator.backend.domain.selfcheck.domain.entity.SelfCheck;
import com.aicodinator.backend.domain.user.domain.entity.User;
import com.aicodinator.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Report extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "self_check_id", unique = true)
    private SelfCheck selfCheck;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "region_id")
    private Region region;

    // 기본 리포트 정보
    private String title;
    private String recommendedLocation;
    private String housingInfo;
    private String aiStory;
    private String recommendedJob;
    private String recommendedPolicy;
    
    // AI 분석 상태 관리
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private ReportStatus status = ReportStatus.PENDING;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    @Column(name = "error_message", length = 500)
    private String errorMessage;
    
    // PRDI (개인화된 실질 가처분 소득)
    @Column(name = "prdi_amount", precision = 15, scale = 0)
    private BigDecimal prdiAmount;
    
    @Column(name = "expected_income", precision = 15, scale = 0)
    private BigDecimal expectedIncome;
    
    @Column(name = "total_subsidy", precision = 15, scale = 0)
    private BigDecimal totalSubsidy;
    
    @Column(name = "housing_cost", precision = 15, scale = 0)
    private BigDecimal housingCost;
    
    @Column(name = "living_cost", precision = 15, scale = 0)
    private BigDecimal livingCost;
    
    @Column(name = "tax_amount", precision = 15, scale = 0)
    private BigDecimal taxAmount;
    
    // AI 추천 지역 목록 (상위 3개)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "recommended_regions", columnDefinition = "json")
    private List<String> recommendedRegions;
    
    // 경제 및 커리어 분석
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "career_analysis", columnDefinition = "json")
    private Map<String, Object> careerAnalysis;
    // 포함 내용: 일자리 매칭률, 예상 연봉, 성장 가능성, 추천 기업 리스트
    
    // 주거 분석
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "housing_analysis", columnDefinition = "json")
    private Map<String, Object> housingAnalysis;
    // 포함 내용: 추천 주거 리스트, 평균 월세, 교통 접근성
    
    // 정책 및 지원금 분석
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "policy_analysis", columnDefinition = "json")
    private Map<String, Object> policyAnalysis;
    // 포함 내용: 받을 수 있는 정책 리스트, 총 지원금액, 신청 방법
    
    // 생활 및 커뮤니티 분석
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "lifestyle_analysis", columnDefinition = "json")
    private Map<String, Object> lifestyleAnalysis;
    // 포함 내용: 지역 성격 프로필, 편의시설, 문화시설, 워라밸 점수
    
    // AI 생성 시나리오
    @Column(name = "daily_scenario", columnDefinition = "TEXT")
    private String dailyScenario; // "창원에서의 하루" 시나리오
    
    @Column(name = "monthly_scenario", columnDefinition = "TEXT")
    private String monthlyScenario; // 월별 생활 시나리오
    
    @Column(name = "yearly_scenario", columnDefinition = "TEXT")
    private String yearlyScenario; // 1년 후 예상 시나리오
    
    // 워라밸 지수
    @Column(name = "work_life_balance_score")
    private Integer workLifeBalanceScore; // 0-100
    
    @Column(name = "commute_time_minutes")
    private Integer commuteTimeMinutes;
    
    // 전체 AI 분석 원본 데이터
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "raw_analysis_data", columnDefinition = "json")
    private Map<String, Object> rawAnalysisData;
    
    // 추천 기업 ID 목록
    @ElementCollection
    @CollectionTable(name = "report_recommended_companies", 
                      joinColumns = @JoinColumn(name = "report_id"))
    @Column(name = "company_id")
    private List<Long> recommendedCompanyIds;
    
    // 추천 주거 ID 목록
    @ElementCollection
    @CollectionTable(name = "report_recommended_housings", 
                      joinColumns = @JoinColumn(name = "report_id"))
    @Column(name = "housing_id")
    private List<Long> recommendedHousingIds;
    
    // 추천 정책 ID 목록
    @ElementCollection
    @CollectionTable(name = "report_recommended_policies", 
                      joinColumns = @JoinColumn(name = "report_id"))
    @Column(name = "policy_id")
    private List<String> recommendedPolicyIds;
    
    // 추천 보조금 ID 목록
    @ElementCollection
    @CollectionTable(name = "report_recommended_subsidies", 
                      joinColumns = @JoinColumn(name = "report_id"))
    @Column(name = "subsidy_id")
    private List<String> recommendedSubsidyIds;
    
    public enum ReportStatus {
        PENDING,     // 생성 요청됨
        PROCESSING,  // AI 분석 중
        COMPLETED,   // 완료
        FAILED       // 실패
    }
    
    // 비즈니스 메서드
    public void startProcessing() {
        this.status = ReportStatus.PROCESSING;
    }
    
    public void completeAnalysis(BigDecimal prdiAmount, 
                                 List<String> recommendedRegions,
                                 Map<String, Object> careerAnalysis,
                                 Map<String, Object> housingAnalysis,
                                 Map<String, Object> policyAnalysis,
                                 Map<String, Object> lifestyleAnalysis) {
        this.status = ReportStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
        this.prdiAmount = prdiAmount;
        this.recommendedRegions = recommendedRegions;
        this.careerAnalysis = careerAnalysis;
        this.housingAnalysis = housingAnalysis;
        this.policyAnalysis = policyAnalysis;
        this.lifestyleAnalysis = lifestyleAnalysis;
    }
    
    public void updateScenarios(String dailyScenario, 
                                String monthlyScenario, 
                                String yearlyScenario) {
        this.dailyScenario = dailyScenario;
        this.monthlyScenario = monthlyScenario;
        this.yearlyScenario = yearlyScenario;
    }
    
    public void updatePrdiDetails(BigDecimal expectedIncome,
                                  BigDecimal totalSubsidy,
                                  BigDecimal housingCost,
                                  BigDecimal livingCost,
                                  BigDecimal taxAmount) {
        this.expectedIncome = expectedIncome;
        this.totalSubsidy = totalSubsidy;
        this.housingCost = housingCost;
        this.livingCost = livingCost;
        this.taxAmount = taxAmount;
        this.prdiAmount = expectedIncome.add(totalSubsidy)
                            .subtract(taxAmount)
                            .subtract(housingCost)
                            .subtract(livingCost);
    }
    
    public void updateRecommendations(List<Long> companyIds,
                                      List<Long> housingIds,
                                      List<String> policyIds,
                                      List<String> subsidyIds) {
        this.recommendedCompanyIds = companyIds;
        this.recommendedHousingIds = housingIds;
        this.recommendedPolicyIds = policyIds;
        this.recommendedSubsidyIds = subsidyIds;
    }
    
    public void fail(String errorMessage) {
        this.status = ReportStatus.FAILED;
        this.errorMessage = errorMessage;
        this.completedAt = LocalDateTime.now();
    }
}