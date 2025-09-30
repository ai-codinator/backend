package com.aicodinator.backend.domain.housing.domain.entity;

import com.aicodinator.backend.domain.region.domain.entity.Region;
import com.aicodinator.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "housings")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Housing extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String address;
    
    @Column(name = "rent_details", nullable = false, length = 100)
    private String rentDetails;
    
    @Column(name = "housing_type", nullable = false, length = 100)
    private String housingType;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;
}