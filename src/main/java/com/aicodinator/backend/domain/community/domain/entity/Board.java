package com.aicodinator.backend.domain.community.domain.entity;

import com.aicodinator.backend.domain.community.domain.constant.BoardType;
import com.aicodinator.backend.domain.region.domain.Region;
import com.aicodinator.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    // private String name; - 게시판 이름 필요하면 추가
}
