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
public interface SubsidyRepository extends JpaRepository<Subsidy, String> {
    
    // 지역별 보조금 조회
    List<Subsidy> findByRegion(String region);
    
    // 기관별 보조금 조회
    List<Subsidy> findByOrganization(String organization);
    
    // 지역과 기관으로 조회
    List<Subsidy> findByRegionAndOrganization(String region, String organization);
    
    // 서비스명 검색
    List<Subsidy> findByServiceNameContaining(String keyword);
    
    // 지원 대상으로 검색
    @Query("SELECT s FROM Subsidy s WHERE s.supportTarget LIKE %:keyword%")
    List<Subsidy> findBySupportTargetContaining(@Param("keyword") String keyword);
    
    // 지원 내용으로 검색
    @Query("SELECT s FROM Subsidy s WHERE s.supportContent LIKE %:keyword%")
    List<Subsidy> findBySupportContentContaining(@Param("keyword") String keyword);
    
    // ID 목록으로 조회
    List<Subsidy> findByServiceIdIn(List<String> serviceIds);
    
    // 페이징 처리된 지역별 보조금 조회
    Page<Subsidy> findByRegion(String region, Pageable pageable);
    
    // 지역별 보조금 개수
    Long countByRegion(String region);
}