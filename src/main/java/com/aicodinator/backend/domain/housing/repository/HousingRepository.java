package com.aicodinator.backend.domain.housing.repository;

import com.aicodinator.backend.domain.housing.domain.entity.Housing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HousingRepository extends JpaRepository<Housing, Long> {
    
    // 지역별 주거 조회
    List<Housing> findByRegion_Id(Long regionId);
    
    // 지역명으로 주거 조회
    List<Housing> findByRegion_Name(String regionName);
    
    // 주거 타입으로 조회 (원룸, 투룸, 오피스텔 등)
    List<Housing> findByHousingType(String housingType);
    
    // 지역과 주거 타입으로 조회
    List<Housing> findByRegion_IdAndHousingType(Long regionId, String housingType);
    
    // 월세 범위로 조회 (rentDetails에서 금액 추출은 서비스 레이어에서 처리)
    @Query("SELECT h FROM Housing h WHERE h.region.id = :regionId AND h.rentDetails LIKE %:keyword%")
    List<Housing> findByRegionAndRentKeyword(@Param("regionId") Long regionId, 
                                              @Param("keyword") String keyword);
    
    // 주소로 검색
    List<Housing> findByAddressContaining(String address);
    
    // 페이징 처리된 지역별 주거 조회
    Page<Housing> findByRegion_Id(Long regionId, Pageable pageable);
    
    // ID 목록으로 조회
    List<Housing> findByIdIn(List<Long> ids);
    
    // 지역별 주거 타입 통계
    @Query("SELECT h.housingType, COUNT(h) FROM Housing h WHERE h.region.id = :regionId GROUP BY h.housingType")
    List<Object[]> countByHousingTypeInRegion(@Param("regionId") Long regionId);
}