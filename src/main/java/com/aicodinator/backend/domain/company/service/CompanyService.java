package com.aicodinator.backend.domain.company.service;

import com.aicodinator.backend.domain.company.domain.entity.Company;
import com.aicodinator.backend.domain.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {
    
    private final CompanyRepository companyRepository;
    
    // 단건 조회
    public Optional<Company> findById(Long id) {
        return companyRepository.findById(id);
    }
    
    // 전체 조회
    public List<Company> findAll() {
        return companyRepository.findAll();
    }
    
    // 지역별 기업 조회
    public List<Company> findByRegion(Long regionId) {
        return companyRepository.findByRegion_Id(regionId);
    }
    
    // 지역명으로 기업 조회
    public List<Company> findByRegionName(String regionName) {
        return companyRepository.findByRegion_Name(regionName);
    }
    
    // 직무로 기업 검색
    public List<Company> findByJobRole(String jobRole) {
        return companyRepository.findByJobRolesContaining(jobRole);
    }
    
    // 지역과 직무로 기업 검색
    public List<Company> findByRegionAndJobRole(Long regionId, String jobRole) {
        return companyRepository.findByRegionAndJobRole(regionId, jobRole);
    }
    
    // 회사명으로 검색
    public List<Company> searchByCompanyName(String keyword) {
        return companyRepository.findByCompanyNameContaining(keyword);
    }
    
    // 페이징 처리된 지역별 기업 조회
    public Page<Company> findByRegionWithPaging(Long regionId, Pageable pageable) {
        return companyRepository.findByRegion_Id(regionId, pageable);
    }
    
    // ID 목록으로 조회 (리포트에서 추천 기업 조회용)
    public List<Company> findByIds(List<Long> ids) {
        return companyRepository.findByIdIn(ids);
    }
    
    @Transactional
    public Company save(Company company) {
        return companyRepository.save(company);
    }
    
    @Transactional
    public void delete(Long id) {
        companyRepository.deleteById(id);
    }
}