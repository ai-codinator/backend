package com.aicodinator.backend.domain.company.controller;

import com.aicodinator.backend.domain.company.domain.entity.Company;
import com.aicodinator.backend.domain.company.service.CompanyService;
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

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@Tag(name = "Company", description = "기업 정보 API")
public class CompanyController {
    
    private final CompanyService companyService;
    
    @GetMapping("/{id}")
    @Operation(summary = "기업 단건 조회", description = "ID로 기업 정보를 조회합니다")
    public ResponseEntity<Company> getCompany(@PathVariable Long id) {
        return companyService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    @Operation(summary = "기업 목록 조회", description = "조건에 맞는 기업 목록을 조회합니다")
    public ResponseEntity<List<Company>> getCompanies(
            @Parameter(description = "지역 ID") @RequestParam(required = false) Long regionId,
            @Parameter(description = "지역명") @RequestParam(required = false) String regionName,
            @Parameter(description = "직무 키워드") @RequestParam(required = false) String jobRole,
            @Parameter(description = "회사명 키워드") @RequestParam(required = false) String companyName) {
        
        List<Company> companies;
        
        if (regionId != null && jobRole != null) {
            companies = companyService.findByRegionAndJobRole(regionId, jobRole);
        } else if (regionId != null) {
            companies = companyService.findByRegion(regionId);
        } else if (regionName != null) {
            companies = companyService.findByRegionName(regionName);
        } else if (jobRole != null) {
            companies = companyService.findByJobRole(jobRole);
        } else if (companyName != null) {
            companies = companyService.searchByCompanyName(companyName);
        } else {
            companies = companyService.findAll();
        }
        
        return ResponseEntity.ok(companies);
    }
    
    @GetMapping("/paged")
    @Operation(summary = "기업 목록 페이징 조회", description = "페이징 처리된 기업 목록을 조회합니다")
    public ResponseEntity<Page<Company>> getCompaniesPaged(
            @Parameter(description = "지역 ID") @RequestParam Long regionId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<Company> companies = companyService.findByRegionWithPaging(regionId, pageable);
        return ResponseEntity.ok(companies);
    }
    
    @GetMapping("/batch")
    @Operation(summary = "기업 일괄 조회", description = "ID 목록으로 여러 기업을 한번에 조회합니다")
    public ResponseEntity<List<Company>> getCompaniesByIds(
            @Parameter(description = "기업 ID 목록") @RequestParam List<Long> ids) {
        
        List<Company> companies = companyService.findByIds(ids);
        return ResponseEntity.ok(companies);
    }
}