package com.aicodinator.backend.domain.community.domain.entity;

import com.aicodinator.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000) // 글자수 체크
    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostFile> files = new ArrayList<>();

    public void addFile(PostFile postFile) {
        files.add(postFile);
        postFile.setPost(this);
    }
}
