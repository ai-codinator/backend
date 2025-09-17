package com.aicodinator.backend.domain.community.domain.entity;

import com.aicodinator.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostFile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(nullable = false)
    private String originalFileName; // 사용자가 업로드한 파일의 원래 이름

    @Column(nullable = false)
    private String uploadUrl; // S3에 업로드된 후 접근할 수 있는 URL

    private long fileSize; // 파일 크기
}
