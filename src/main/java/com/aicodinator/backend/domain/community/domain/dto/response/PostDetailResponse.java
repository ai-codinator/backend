package com.aicodinator.backend.domain.community.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostDetailResponse {
    private Long postId;

    private String title;

    private String content;

    private String authorName;

    private List<PostFileResponse> files;

    private Boolean isOwner;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
