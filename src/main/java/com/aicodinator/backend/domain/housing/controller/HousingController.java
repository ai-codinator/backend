package com.aicodinator.backend.domain.housing.controller;

import com.aicodinator.backend.domain.housing.domain.entity.Housing;
import com.aicodinator.backend.domain.housing.service.HousingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/housings")
@RequiredArgsConstructor
@Tag(name = "Housing", description = "주거 정보 API")
public class HousingController {
    
    private final HousingService housingService;
    
    @GetMapping("/{id}")
    @Operation(summary = "주거 단건 조회", description = "ID로 주거 정보를 조회합니다")
    public ResponseEntity<Housing> getHousing(@PathVariable Long id) {
        return housingService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    @Operation(summary = "주거 목록 조회", description = "조건에 맞는 주거 목록을 조회합니다")
    public ResponseEntity<List<Housing>> getHousings(
            @Parameter(description = "지역 ID") @RequestParam(required = false) Long regionId,
            @Parameter(description = "지역명") @RequestParam(required = false) String regionName,
            @Parameter(description = "주거 타입") @RequestParam(required = false) String housingType,
            @Parameter(description = "최대 월세 (만원)") @RequestParam(required = false) Integer maxRent,
            @Parameter(description = "주소 검색 키워드") @RequestParam(required = false) String address) {
        
        List<Housing> housings;
        
        if (regionId != null && housingType != null) {
            housings = housingService.findByRegionAndType(regionId, housingType);
        } else if (regionId != null && maxRent != null) {
            housings = housingService.findByRegionAndMaxRent(regionId, maxRent);
        } else if (regionId != null) {
            housings = housingService.findByRegion(regionId);
        } else if (regionName != null) {
            housings = housingService.findByRegionName(regionName);
        } else if (housingType != null) {
            housings = housingService.findByHousingType(housingType);
        } else if (address != null) {
            housings = housingService.searchByAddress(address);
        } else {
            housings = housingService.findAll();
        }
        
        return ResponseEntity.ok(housings);
    }
    
    @GetMapping("/paged")
    @Operation(summary = "주거 목록 페이징 조회", description = "페이징 처리된 주거 목록을 조회합니다")
    public ResponseEntity<Page<Housing>> getHousingsPaged(
            @Parameter(description = "지역 ID") @RequestParam Long regionId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<Housing> housings = housingService.findByRegionWithPaging(regionId, pageable);
        return ResponseEntity.ok(housings);
    }
    
    @GetMapping("/batch")
    @Operation(summary = "주거 일괄 조회", description = "ID 목록으로 여러 주거 정보를 한번에 조회합니다")
    public ResponseEntity<List<Housing>> getHousingsByIds(
            @Parameter(description = "주거 ID 목록") @RequestParam List<Long> ids) {
        
        List<Housing> housings = housingService.findByIds(ids);
        return ResponseEntity.ok(housings);
    }
    
    @GetMapping("/statistics")
    @Operation(summary = "주거 타입 통계", description = "지역별 주거 타입 통계를 조회합니다")
    public ResponseEntity<Map<String, Long>> getHousingStatistics(
            @Parameter(description = "지역 ID") @RequestParam Long regionId) {
        
        Map<String, Long> statistics = housingService.getHousingTypeStatistics(regionId);
        return ResponseEntity.ok(statistics);
    }
}