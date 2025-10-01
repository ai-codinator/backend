import jakarta.persistence.*;
import lombok.*;

// Subsidy 엔티티와 똑같은 구조로 테스트
@Entity
@Table(name = "test_subsidies")
class TestSubsidy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "subsidy_name", nullable = false)
    private String subsidyName;
    
    @Column(name = "subsidy_type", nullable = false, length = 50)
    private String subsidyType;
    
    @Column(name = "target_region", length = 50)
    private String targetRegion;
    
    @Column(columnDefinition = "TEXT")
    private String eligibility;
    
    @Column(length = 100)
    private String amount;
    
    @Column(name = "application_period", length = 100)
    private String applicationPeriod;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "application_url")
    private String applicationUrl;
}

// Spring Boot의 기본 네이밍 전략으로 생성되는 컬럼명:
// ID
// SUBSIDY_NAME (명시적 @Column)
// SUBSIDY_TYPE (명시적 @Column)  
// TARGET_REGION (명시적 @Column)
// ELIGIBILITY (camelCase → UPPER_SNAKE_CASE)
// AMOUNT (camelCase → UPPER_SNAKE_CASE)
// APPLICATION_PERIOD (명시적 @Column)
// DESCRIPTION (camelCase → UPPER_SNAKE_CASE)
// APPLICATION_URL (명시적 @Column)