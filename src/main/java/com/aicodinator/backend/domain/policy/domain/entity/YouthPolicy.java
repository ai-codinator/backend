package com.aicodinator.backend.domain.policy.domain.entity;

import com.aicodinator.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "youth_policies")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YouthPolicy extends BaseEntity {
    
    @Id
    @Column(name = "policy_id", length = 50)
    private String policyId;
    
    @Column(name = "policy_name", nullable = false, length = 300)
    private String policyName;
    
    @Column(name = "policy_field", length = 100)
    private String policyField;
    
    @Column(name = "policy_introduction", columnDefinition = "TEXT")
    private String policyIntroduction;
    
    @Column(name = "support_content", columnDefinition = "TEXT")
    private String supportContent;
    
    @Column(name = "support_target", length = 500)
    private String supportTarget;
    
    @Column(name = "organization", length = 200)
    private String organization;
    
    @Column(name = "region", length = 100)
    private String region;
    
    @Column(name = "min_age")
    private Integer minAge;
    
    @Column(name = "max_age")
    private Integer maxAge;
}