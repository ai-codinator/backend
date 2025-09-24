package com.aicodinator.backend.domain.community.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostListResponse {
    private Long postId;

    private String title;

    private long viewCount;

    private long likeCount;

    private int commentCount;
}
