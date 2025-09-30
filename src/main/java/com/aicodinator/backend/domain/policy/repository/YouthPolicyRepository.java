package com.aicodinator.backend.domain.policy.repository;

import com.aicodinator.backend.domain.policy.domain.entity.YouthPolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface YouthPolicyRepository extends JpaRepository<YouthPolicy, String> {
    
    // 지역별 정책 조회
    List<YouthPolicy> findByRegion(String region);
    
    // 정책 분야별 조회
    List<YouthPolicy> findByPolicyField(String policyField);
    
    // 지역과 분야로 조회
    List<YouthPolicy> findByRegionAndPolicyField(String region, String policyField);
    
    // 나이 조건에 맞는 정책 조회
    @Query("SELECT yp FROM YouthPolicy yp WHERE " +
           "(yp.minAge IS NULL OR yp.minAge <= :age) AND " +
           "(yp.maxAge IS NULL OR yp.maxAge >= :age)")
    List<YouthPolicy> findByAgeEligible(@Param("age") Integer age);
    
    // 지역과 나이 조건으로 조회
    @Query("SELECT yp FROM YouthPolicy yp WHERE " +
           "yp.region = :region AND " +
           "(yp.minAge IS NULL OR yp.minAge <= :age) AND " +
           "(yp.maxAge IS NULL OR yp.maxAge >= :age)")
    List<YouthPolicy> findByRegionAndAge(@Param("region") String region, 
                                          @Param("age") Integer age);
    
    // 정책명 검색
    List<YouthPolicy> findByPolicyNameContaining(String keyword);
    
    // 기관별 정책 조회
    List<YouthPolicy> findByOrganization(String organization);
    
    // ID 목록으로 조회
    List<YouthPolicy> findByPolicyIdIn(List<String> policyIds);
    
    // 페이징 처리된 지역별 정책 조회
    Page<YouthPolicy> findByRegion(String region, Pageable pageable);
    
    // 지원 대상 키워드 검색
    @Query("SELECT yp FROM YouthPolicy yp WHERE yp.supportTarget LIKE %:keyword%")
    List<YouthPolicy> findBySupportTargetContaining(@Param("keyword") String keyword);
}