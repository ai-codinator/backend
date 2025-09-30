package com.aicodinator.backend.domain.housing.service;

import com.aicodinator.backend.domain.housing.domain.entity.Housing;
import com.aicodinator.backend.domain.housing.repository.HousingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HousingService {
    
    private final HousingRepository housingRepository;
    
    // 단건 조회
    public Optional<Housing> findById(Long id) {
        return housingRepository.findById(id);
    }
    
    // 전체 조회
    public List<Housing> findAll() {
        return housingRepository.findAll();
    }
    
    // 지역별 주거 조회
    public List<Housing> findByRegion(Long regionId) {
        return housingRepository.findByRegion_Id(regionId);
    }
    
    // 지역명으로 주거 조회
    public List<Housing> findByRegionName(String regionName) {
        return housingRepository.findByRegion_Name(regionName);
    }
    
    // 주거 타입으로 조회
    public List<Housing> findByHousingType(String housingType) {
        return housingRepository.findByHousingType(housingType);
    }
    
    // 지역과 주거 타입으로 조회
    public List<Housing> findByRegionAndType(Long regionId, String housingType) {
        return housingRepository.findByRegion_IdAndHousingType(regionId, housingType);
    }
    
    // 월세 범위로 필터링 (서비스 레이어에서 처리)
    public List<Housing> findByRegionAndMaxRent(Long regionId, Integer maxRent) {
        List<Housing> housings = housingRepository.findByRegion_Id(regionId);
        
        return housings.stream()
            .filter(h -> extractMonthlyRent(h.getRentDetails()) <= maxRent)
            .collect(Collectors.toList());
    }
    
    // 주소로 검색
    public List<Housing> searchByAddress(String keyword) {
        return housingRepository.findByAddressContaining(keyword);
    }
    
    // 페이징 처리된 지역별 주거 조회
    public Page<Housing> findByRegionWithPaging(Long regionId, Pageable pageable) {
        return housingRepository.findByRegion_Id(regionId, pageable);
    }
    
    // ID 목록으로 조회 (리포트에서 추천 주거 조회용)
    public List<Housing> findByIds(List<Long> ids) {
        return housingRepository.findByIdIn(ids);
    }
    
    // 지역별 주거 타입 통계
    public Map<String, Long> getHousingTypeStatistics(Long regionId) {
        List<Object[]> stats = housingRepository.countByHousingTypeInRegion(regionId);
        return stats.stream()
            .collect(Collectors.toMap(
                s -> (String) s[0],
                s -> (Long) s[1]
            ));
    }
    
    @Transactional
    public Housing save(Housing housing) {
        return housingRepository.save(housing);
    }
    
    @Transactional
    public void delete(Long id) {
        housingRepository.deleteById(id);
    }
    
    // 월세 금액 추출 헬퍼 메서드
    private Integer extractMonthlyRent(String rentDetails) {
        // "보증금 500만 / 월세 45만" 형식에서 월세 추출
        if (rentDetails == null || !rentDetails.contains("월세")) {
            return Integer.MAX_VALUE;
        }
        
        try {
            String[] parts = rentDetails.split("월세");
            if (parts.length > 1) {
                String rentPart = parts[1].replaceAll("[^0-9]", "");
                return Integer.parseInt(rentPart);
            }
        } catch (Exception e) {
            // 파싱 실패시 최대값 반환
        }
        
        return Integer.MAX_VALUE;
    }
}