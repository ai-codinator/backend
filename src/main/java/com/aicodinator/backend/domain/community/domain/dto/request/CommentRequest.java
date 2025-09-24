package com.aicodinator.backend.domain.community.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CommentRequest {
    @NotNull(message = "게시글 Id는 필수입니다.")
    private Long postId;

    @NotBlank(message = "댓글 내용은 필수입니다.")
    @Size(max = 100, message = "댓글은 100자 이하로 작성해주세요.")
    private String content;
}
