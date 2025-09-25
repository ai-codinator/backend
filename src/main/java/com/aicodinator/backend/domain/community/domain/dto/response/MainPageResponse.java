package com.aicodinator.backend.domain.community.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MainPageResponse {
    private List<PostListResponse> latestPosts;         // 최신글

    private List<PostListResponse> popularPosts;        // 인기글

    private List<PostListResponse> latestCommentPosts;  // 최신 댓글이 달린 글

    private List<PostListResponse> mostCommentedPosts;  // 댓글 많은 글
}
