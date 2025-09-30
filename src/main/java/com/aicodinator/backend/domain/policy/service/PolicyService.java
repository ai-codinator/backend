package com.aicodinator.backend.domain.policy.service;

import com.aicodinator.backend.domain.policy.domain.entity.Subsidy;
import com.aicodinator.backend.domain.policy.domain.entity.YouthPolicy;
import com.aicodinator.backend.domain.policy.repository.SubsidyRepository;
import com.aicodinator.backend.domain.policy.repository.YouthPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PolicyService {
    
    private final YouthPolicyRepository youthPolicyRepository;
    private final SubsidyRepository subsidyRepository;
    
    // ===== YouthPolicy 관련 메서드 =====
    
    // 청년정책 단건 조회
    public Optional<YouthPolicy> findYouthPolicyById(String policyId) {
        return youthPolicyRepository.findById(policyId);
    }
    
    // 청년정책 전체 조회
    public List<YouthPolicy> findAllYouthPolicies() {
        return youthPolicyRepository.findAll();
    }
    
    // 지역별 청년정책 조회
    public List<YouthPolicy> findYouthPoliciesByRegion(String region) {
        return youthPolicyRepository.findByRegion(region);
    }
    
    // 나이 조건에 맞는 청년정책 조회
    public List<YouthPolicy> findYouthPoliciesByAge(Integer age) {
        return youthPolicyRepository.findByAgeEligible(age);
    }
    
    // 지역과 나이 조건으로 청년정책 조회
    public List<YouthPolicy> findYouthPoliciesByRegionAndAge(String region, Integer age) {
        return youthPolicyRepository.findByRegionAndAge(region, age);
    }
    
    // 정책 분야별 조회
    public List<YouthPolicy> findYouthPoliciesByField(String field) {
        return youthPolicyRepository.findByPolicyField(field);
    }
    
    // 정책명 검색
    public List<YouthPolicy> searchYouthPolicies(String keyword) {
        return youthPolicyRepository.findByPolicyNameContaining(keyword);
    }
    
    // ID 목록으로 청년정책 조회
    public List<YouthPolicy> findYouthPoliciesByIds(List<String> policyIds) {
        return youthPolicyRepository.findByPolicyIdIn(policyIds);
    }
    
    // 페이징 처리된 지역별 청년정책 조회
    public Page<YouthPolicy> findYouthPoliciesByRegionWithPaging(String region, Pageable pageable) {
        return youthPolicyRepository.findByRegion(region, pageable);
    }
    
    // ===== Subsidy 관련 메서드 =====
    
    // 보조금 단건 조회
    public Optional<Subsidy> findSubsidyById(String serviceId) {
        return subsidyRepository.findById(serviceId);
    }
    
    // 보조금 전체 조회
    public List<Subsidy> findAllSubsidies() {
        return subsidyRepository.findAll();
    }
    
    // 지역별 보조금 조회
    public List<Subsidy> findSubsidiesByRegion(String region) {
        return subsidyRepository.findByRegion(region);
    }
    
    // 기관별 보조금 조회
    public List<Subsidy> findSubsidiesByOrganization(String organization) {
        return subsidyRepository.findByOrganization(organization);
    }
    
    // 서비스명으로 보조금 검색
    public List<Subsidy> searchSubsidies(String keyword) {
        return subsidyRepository.findByServiceNameContaining(keyword);
    }
    
    // 지원 대상으로 보조금 검색
    public List<Subsidy> findSubsidiesByTarget(String targetKeyword) {
        return subsidyRepository.findBySupportTargetContaining(targetKeyword);
    }
    
    // ID 목록으로 보조금 조회
    public List<Subsidy> findSubsidiesByIds(List<String> serviceIds) {
        return subsidyRepository.findByServiceIdIn(serviceIds);
    }
    
    // 페이징 처리된 지역별 보조금 조회
    public Page<Subsidy> findSubsidiesByRegionWithPaging(String region, Pageable pageable) {
        return subsidyRepository.findByRegion(region, pageable);
    }
    
    // 지역별 보조금 개수
    public Long countSubsidiesByRegion(String region) {
        return subsidyRepository.countByRegion(region);
    }
    
    // ===== 통합 조회 메서드 =====
    
    // 지역과 나이로 받을 수 있는 모든 정책/보조금 조회
    public Map<String, Object> findAllAvailablePolicies(String region, Integer age, String targetKeyword) {
        Map<String, Object> result = new HashMap<>();
        
        // 청년정책 조회
        List<YouthPolicy> policies = age != null 
            ? youthPolicyRepository.findByRegionAndAge(region, age)
            : youthPolicyRepository.findByRegion(region);
            
        // 보조금 조회
        List<Subsidy> subsidies = targetKeyword != null
            ? subsidyRepository.findBySupportTargetContaining(targetKeyword)
                .stream()
                .filter(s -> s.getRegion().equals(region))
                .collect(Collectors.toList())
            : subsidyRepository.findByRegion(region);
            
        result.put("youthPolicies", policies);
        result.put("subsidies", subsidies);
        result.put("totalCount", policies.size() + subsidies.size());
        
        return result;
    }
    
    @Transactional
    public YouthPolicy saveYouthPolicy(YouthPolicy policy) {
        return youthPolicyRepository.save(policy);
    }
    
    @Transactional
    public Subsidy saveSubsidy(Subsidy subsidy) {
        return subsidyRepository.save(subsidy);
    }
    
    @Transactional
    public void deleteYouthPolicy(String policyId) {
        youthPolicyRepository.deleteById(policyId);
    }
    
    @Transactional
    public void deleteSubsidy(String serviceId) {
        subsidyRepository.deleteById(serviceId);
    }
}