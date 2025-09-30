package com.aicodinator.backend.domain.policy.domain.entity;

import com.aicodinator.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "subsidies")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Subsidy extends BaseEntity {
    
    @Id
    @Column(name = "service_id", length = 50)
    private String serviceId;
    
    @Column(name = "service_name", nullable = false, length = 500)
    private String serviceName;
    
    @Column(name = "organization", length = 200)
    private String organization;
    
    @Column(name = "region", length = 100)
    private String region;
    
    @Column(name = "support_content", columnDefinition = "TEXT")
    private String supportContent;
    
    @Column(name = "support_target", columnDefinition = "TEXT")
    private String supportTarget;
    
    @Column(name = "apply_method", columnDefinition = "TEXT")
    private String applyMethod;
}