package com.aicodinator.backend.domain.company.domain.entity;

import com.aicodinator.backend.domain.region.domain.entity.Region;
import com.aicodinator.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "companies")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Company extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "company_name", nullable = false, length = 100)
    private String companyName;
    
    @Column(name = "job_roles", nullable = false, length = 200)
    private String jobRoles;
    
    @Column(nullable = false, length = 200)
    private String address;
    
    @Column(name = "salary_entry", length = 50)
    private String salaryEntry;
    
    @Column(name = "salary_avg", length = 50)
    private String salaryAvg;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = true)
    private Region region;
}