package com.aicodinator.backend.domain.policy.repository;

import com.aicodinator.backend.domain.policy.domain.entity.Subsidy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubsidyRepository extends JpaRepository<Subsidy, Long> {
    
    // 지역별 보조금 조회
    List<Subsidy> findByTargetRegion(String targetRegion);
    
    // 유형별 보조금 조회
    List<Subsidy> findBySubsidyType(String subsidyType);
    
    // 지역과 유형으로 조회
    List<Subsidy> findByTargetRegionAndSubsidyType(String targetRegion, String subsidyType);
    
    // 보조금명 검색
    List<Subsidy> findBySubsidyNameContaining(String keyword);
    
    // 자격 조건으로 검색
    @Query("SELECT s FROM Subsidy s WHERE s.eligibility LIKE %:keyword%")
    List<Subsidy> findByEligibilityContaining(@Param("keyword") String keyword);
    
    // 설명으로 검색
    @Query("SELECT s FROM Subsidy s WHERE s.description LIKE %:keyword%")
    List<Subsidy> findByDescriptionContaining(@Param("keyword") String keyword);
    
    // ID 목록으로 조회
    List<Subsidy> findByIdIn(List<Long> ids);
    
    // 페이징 처리된 지역별 보조금 조회
    Page<Subsidy> findByTargetRegion(String targetRegion, Pageable pageable);
    
    // 지역별 보조금 개수
    Long countByTargetRegion(String targetRegion);
}