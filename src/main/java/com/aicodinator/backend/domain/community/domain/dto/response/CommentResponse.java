package com.aicodinator.backend.domain.community.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CommentResponse {
    private Long commentId;

    private String content;

    private String authorName;

    private Boolean isOwner;

    private LocalDateTime createdAt;
}
