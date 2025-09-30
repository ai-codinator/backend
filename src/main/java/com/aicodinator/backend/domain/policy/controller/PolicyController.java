package com.aicodinator.backend.domain.policy.controller;

import com.aicodinator.backend.domain.policy.domain.entity.Subsidy;
import com.aicodinator.backend.domain.policy.domain.entity.YouthPolicy;
import com.aicodinator.backend.domain.policy.service.PolicyService;
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
@RequestMapping("/api/policies")
@RequiredArgsConstructor
@Tag(name = "Policy", description = "정책 및 보조금 API")
public class PolicyController {
    
    private final PolicyService policyService;
    
    // ===== 청년정책 API =====
    
    @GetMapping("/youth/{policyId}")
    @Operation(summary = "청년정책 단건 조회", description = "정책 ID로 청년정책을 조회합니다")
    public ResponseEntity<YouthPolicy> getYouthPolicy(@PathVariable String policyId) {
        return policyService.findYouthPolicyById(policyId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/youth")
    @Operation(summary = "청년정책 목록 조회", description = "조건에 맞는 청년정책 목록을 조회합니다")
    public ResponseEntity<List<YouthPolicy>> getYouthPolicies(
            @Parameter(description = "지역") @RequestParam(required = false) String region,
            @Parameter(description = "나이") @RequestParam(required = false) Integer age,
            @Parameter(description = "정책 분야") @RequestParam(required = false) String field,
            @Parameter(description = "검색 키워드") @RequestParam(required = false) String keyword) {
        
        List<YouthPolicy> policies;
        
        if (region != null && age != null) {
            policies = policyService.findYouthPoliciesByRegionAndAge(region, age);
        } else if (region != null) {
            policies = policyService.findYouthPoliciesByRegion(region);
        } else if (age != null) {
            policies = policyService.findYouthPoliciesByAge(age);
        } else if (field != null) {
            policies = policyService.findYouthPoliciesByField(field);
        } else if (keyword != null) {
            policies = policyService.searchYouthPolicies(keyword);
        } else {
            policies = policyService.findAllYouthPolicies();
        }
        
        return ResponseEntity.ok(policies);
    }
    
    @GetMapping("/youth/batch")
    @Operation(summary = "청년정책 일괄 조회", description = "ID 목록으로 여러 청년정책을 한번에 조회합니다")
    public ResponseEntity<List<YouthPolicy>> getYouthPoliciesByIds(
            @Parameter(description = "정책 ID 목록") @RequestParam List<String> ids) {
        
        List<YouthPolicy> policies = policyService.findYouthPoliciesByIds(ids);
        return ResponseEntity.ok(policies);
    }
    
    @GetMapping("/youth/paged")
    @Operation(summary = "청년정책 페이징 조회", description = "페이징 처리된 청년정책 목록을 조회합니다")
    public ResponseEntity<Page<YouthPolicy>> getYouthPoliciesPaged(
            @Parameter(description = "지역") @RequestParam String region,
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<YouthPolicy> policies = policyService.findYouthPoliciesByRegionWithPaging(region, pageable);
        return ResponseEntity.ok(policies);
    }
    
    // ===== 보조금 API =====
    
    @GetMapping("/subsidy/{serviceId}")
    @Operation(summary = "보조금 단건 조회", description = "서비스 ID로 보조금을 조회합니다")
    public ResponseEntity<Subsidy> getSubsidy(@PathVariable String serviceId) {
        return policyService.findSubsidyById(serviceId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/subsidies")
    @Operation(summary = "보조금 목록 조회", description = "조건에 맞는 보조금 목록을 조회합니다")
    public ResponseEntity<List<Subsidy>> getSubsidies(
            @Parameter(description = "지역") @RequestParam(required = false) String region,
            @Parameter(description = "기관") @RequestParam(required = false) String organization,
            @Parameter(description = "대상 키워드") @RequestParam(required = false) String targetKeyword,
            @Parameter(description = "검색 키워드") @RequestParam(required = false) String keyword) {
        
        List<Subsidy> subsidies;
        
        if (region != null) {
            subsidies = policyService.findSubsidiesByRegion(region);
        } else if (organization != null) {
            subsidies = policyService.findSubsidiesByOrganization(organization);
        } else if (targetKeyword != null) {
            subsidies = policyService.findSubsidiesByTarget(targetKeyword);
        } else if (keyword != null) {
            subsidies = policyService.searchSubsidies(keyword);
        } else {
            subsidies = policyService.findAllSubsidies();
        }
        
        return ResponseEntity.ok(subsidies);
    }
    
    @GetMapping("/subsidies/batch")
    @Operation(summary = "보조금 일괄 조회", description = "ID 목록으로 여러 보조금을 한번에 조회합니다")
    public ResponseEntity<List<Subsidy>> getSubsidiesByIds(
            @Parameter(description = "서비스 ID 목록") @RequestParam List<String> ids) {
        
        List<Subsidy> subsidies = policyService.findSubsidiesByIds(ids);
        return ResponseEntity.ok(subsidies);
    }
    
    @GetMapping("/subsidies/paged")
    @Operation(summary = "보조금 페이징 조회", description = "페이징 처리된 보조금 목록을 조회합니다")
    public ResponseEntity<Page<Subsidy>> getSubsidiesPaged(
            @Parameter(description = "지역") @RequestParam String region,
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<Subsidy> subsidies = policyService.findSubsidiesByRegionWithPaging(region, pageable);
        return ResponseEntity.ok(subsidies);
    }
    
    @GetMapping("/subsidies/count")
    @Operation(summary = "지역별 보조금 개수", description = "특정 지역의 보조금 개수를 조회합니다")
    public ResponseEntity<Long> countSubsidies(
            @Parameter(description = "지역") @RequestParam String region) {
        
        Long count = policyService.countSubsidiesByRegion(region);
        return ResponseEntity.ok(count);
    }
    
    // ===== 통합 API =====
    
    @GetMapping("/all")
    @Operation(summary = "전체 정책 통합 조회", description = "청년정책과 보조금을 통합하여 조회합니다")
    public ResponseEntity<Map<String, Object>> getAllPolicies(
            @Parameter(description = "지역", required = true) @RequestParam String region,
            @Parameter(description = "나이") @RequestParam(required = false) Integer age,
            @Parameter(description = "대상 키워드") @RequestParam(required = false) String targetKeyword) {
        
        Map<String, Object> result = policyService.findAllAvailablePolicies(region, age, targetKeyword);
        return ResponseEntity.ok(result);
    }
}