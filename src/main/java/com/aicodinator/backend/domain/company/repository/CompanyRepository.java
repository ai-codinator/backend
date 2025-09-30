package com.aicodinator.backend.domain.company.repository;

import com.aicodinator.backend.domain.company.domain.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    
    // 지역별 기업 조회
    List<Company> findByRegion_Id(Long regionId);
    
    // 지역명으로 기업 조회
    List<Company> findByRegion_Name(String regionName);
    
    // 직무 포함 검색 (LIKE 검색)
    @Query("SELECT c FROM Company c WHERE c.jobRoles LIKE %:jobRole%")
    List<Company> findByJobRolesContaining(@Param("jobRole") String jobRole);
    
    // 지역과 직무로 검색
    @Query("SELECT c FROM Company c WHERE c.region.id = :regionId AND c.jobRoles LIKE %:jobRole%")
    List<Company> findByRegionAndJobRole(@Param("regionId") Long regionId, 
                                          @Param("jobRole") String jobRole);
    
    // 회사명으로 검색
    List<Company> findByCompanyNameContaining(String companyName);
    
    // 페이징 처리된 지역별 기업 조회
    Page<Company> findByRegion_Id(Long regionId, Pageable pageable);
    
    // ID 목록으로 조회
    List<Company> findByIdIn(List<Long> ids);
}