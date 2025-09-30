package com.aicodinator.backend.domain.common.domain.entity;

import com.aicodinator.backend.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "code")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Code extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;        // MBTI, OCCP, TASK
    private String code;        // 코드 값
    private String name;        // 코드명 (한글명)
    private String description; // 설명
    private boolean active;     // 활성화 여부
    private int sortOrder;      // 정렬 순서
}
